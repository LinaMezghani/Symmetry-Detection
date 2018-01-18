package _3D;

import java.util.LinkedList;

import Jcg.geometry.*;

/**
 * This class contains the main methods implementing the Mean-Shift clustering
 * 
 * @author Luca Castelli Aleardi and Steve Oudot (Ecole Polytechnique, INF562)
 * @version jan 2014
 */
public class MeanShiftClustering implements Clustering {
	
	// describes how points are assigned to clusters
	public Cluster<Point_3> clusters=new Cluster<Point_3>();
	
	PointCloud N; // input point cloud
	PointCloud seeds;
	double sqCvgRad;  // convergence radius
	double sqAvgRad;  // averaging radius (definining the window)
	double sqInflRad;  // influence radius
	double sqMergeRad;  // merging radius

	RangeSearch<Point_3> Rs;  // data structure for nearest neighbor search


	// Constructors

	void initMSC (PointCloud n, PointCloud s, double cr, double ar, double ir, double mr){
		N = n;
		seeds = s;
		sqCvgRad = cr*cr;
		sqAvgRad = ar*ar;
		sqInflRad = ir*ir;
		sqMergeRad = mr*mr;
		Rs = new SlowRangeSearch<Point_3>(N);
	}

	MeanShiftClustering (PointCloud n, PointCloud s, 
			double cr, double ar, double ir, double mr) {
		initMSC (n,s,cr,ar,ir, mr);
	}

	public MeanShiftClustering (PointCloud n, double bandWidth) {
		initMSC (n, n, bandWidth*1e-3, bandWidth, bandWidth/4, bandWidth);
	}
	
	public Cluster<Point_3> getClusters() {
		return this.clusters;
	}

	/**
	 * Single cluster detection -- returns approximate peak and cluster points
	 * The output is a list of point containing:
	 *  - all the points belonging to the detected cluster
	 *  - the peak point (at the top of the list)
	 */
	public PointCloud detectCluster (Point_3 seed, int clusterIndex) {
		//COMPLETE
		LinkedList<Point_3> currentSeeds = new LinkedList<Point_3>(); //La liste des xi successifs
		currentSeeds.add(seed);
		PointCloud tmp;
		//La graine fera partie du cluster final (notre graine appartient à N)
		PointCloud res = null;

		//On boucle jusqu'à trouver le point stationnaire (d(xi+1,xi)^2<=Rc^2)
		do{ //Pour ne pas calculer distance la 1ère fois (currentSeeds.get(1) == null)
			//On calcule le voisinage de xi
			tmp = Rs.OrthogonalRangeSearch(currentSeeds.peek(), sqAvgRad);
			//Si on a trouvé aucun point dans le cluster (cas des pixels bizarres comme le 252 de l'image
			//house.png dont les valeurs sont (0.0, NaN, NaN))
			if(tmp==null) {
				//seed.cluster = clusterIndex;
				this.clusters.addToCluster(seed, clusterIndex);
				res = new PointCloud(seed, res, false);
				break;
			}
			//On calcule le nouveau point : xi+1 = mean{neighbors(xi)}
			currentSeeds.addFirst(PointCloud.mean(tmp));
		}while(PointCloud.myDistance(currentSeeds.get(1),currentSeeds.peek()) > sqCvgRad);

		//On reparcours la liste des seed successives en associant leur voisins (points
		//dans le rayon d'influence) au cluster s'ils n'appartiennent pas déjà à un cluster
		for(Point_3 v : currentSeeds){
			//Pour le point stationnaire : on ajoute au cluster tous les points dans la fenêtre de rayon 
			//sqCvgRad autour de lui (pour les autres : sqInflRad)
			if(v == currentSeeds.peek()) tmp = Rs.OrthogonalRangeSearch(v, sqAvgRad); //AMELIORATION
			else tmp = Rs.OrthogonalRangeSearch(v, sqInflRad);                        //AMELIORATION
			for (PointCloud n = tmp; n != null; n = n.next){
				if(this.clusters.getClusterIndex(n.p) != -1) 
					continue; // point is already assigned to a cluster
				this.clusters.addToCluster(n.p, clusterIndex); // assign point p to a given cluster
				res = new PointCloud(n.p, res, false);
			}
		}
		//On ajoute au PointCloud résultat le point stationnaire (le dernier point ajouté en tête de currentSeeds)
		return new PointCloud(currentSeeds.peek(), res, false);
	}


	/**
	 * Perform cluster merging
	 * Remark: cluster center is at top of cluster cloud
	 * @param cluster the point cloud to be merge (cluster), containing at the top its center
	 * @param clusterCenters[] the set (array) of cluster centers
	 * @return -1 if no merge is performed, 'i' if the input cluster has been associated with the cluster i (already existing)
	 */
	public int mergeCluster (PointCloud cluster, Point_3[] clusterCenters) {
		//COMPLETE
		int i=0;
		while(clusterCenters[i]!=null){
			if(PointCloud.myDistance(cluster.p,clusterCenters[i]) <= sqMergeRad){
				//Si les deux peaks sont proches : on fusionne les clusters
				for (PointCloud n = cluster; n != null; n = n.next) {
					this.clusters.addToCluster(n.p, i);
				}
				break; //On ne fusionne qu'avec un cluster
			}
			i++;
		}
		if(clusterCenters[i]==null) return -1; //Aucune fusion
		return i;
	}


	/**
	 * Main algorithm for detecting all clusters
	 * Clusters are detected iteratively (until all points are processed)
	 * Clusters are merged if required: when the corresponding peaks are close
	 * 
	 * @return Point_3[] an array of points, of size n .First i elements are cluster centers (non null points), remaining n-i elements must be null
	 */
	public Point_3[] computeClusters () {
		//COMPLETE
		//On crï¿½e le tableau clusterCenters
		int nbPoints = PointCloud.size(N);
		Point_3[] clusterCenters = new Point_3[nbPoints];
		//Initialisation ï¿½ null
		for(int i=0; i<nbPoints; i++){
			clusterCenters[i]=null;
		}
		/*for (PointCloud n = N; n != null; n = n.next) { // not needed with hash tables
			n.p.cluster = -1;
		}*/
		//Nombre de clusters
		int nbClusters = 0;
		//Tant qu'il existe des graines non classifies, on choisit la 1ï¿½re
		while(seeds!=null){
			if(this.clusters.getClusterIndex(seeds.p) == -1){
				PointCloud l = detectCluster(seeds.p, nbClusters);
				//Si on ne peut pas fusionner ce cluster avec un cluster dï¿½jï¿½ existant
				if(mergeCluster(l, clusterCenters) == -1){
					clusterCenters[nbClusters] = l.p;
					nbClusters++;
				}
			}
			seeds = seeds.next;
		}
		//Si on veut renvoyer un tableau de la bonne taille pour afficher le nombre de clusters trouvés
		//Arrays.copyOf(clusterCenters,nbClusters);
		return clusterCenters;
	}

}

