import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class to perform operations on a undirected and conected Graph (from a input file)
 * @author barbara.lopes
 *
 */
public class Graph {

	private List<Integer>[] accessMatrix, adjacencyMatrix;
	private int vertexCount, accessCount, edgesCount;

	/**
	 * Constructor
	 */
	public Graph(){

	}

	@SuppressWarnings("unchecked")
	public Graph(int vertexCount) {
		this.vertexCount = vertexCount;
		adjacencyMatrix = new ArrayList[vertexCount];
		accessMatrix = new ArrayList[vertexCount]; 
	}

	/**
	 * Add a edge on the AdjacencyMatrix
	 * @param i - vertex 1
	 * @param j - vertex 2
	 */
	public void addEdge(int i, int j) {
		
		if (i > 0 && i <= vertexCount) {
			List<Integer> adjacencies = new ArrayList<Integer>();
			
			// If vertex still doesn't have any edge connected to it 
			if(adjacencyMatrix[i - 1] == null){
				
				adjacencies = new ArrayList<Integer>();

				// Initialize adjacencies
				for(int a = 0; a < vertexCount; a++){
					adjacencies.add(0);
				}
			}
			else{
				adjacencies = adjacencyMatrix[i - 1];
			}

			// Set Adjacency (add edge on the matrix)
			adjacencies.set(j - 1, 1);
			adjacencyMatrix[i - 1] = adjacencies;

			/**
			 * Mirroring
			 */
			
			// If vertex still doesn't have any edge connected to it 
			if(adjacencyMatrix[j - 1] == null){
				
				adjacencies = new ArrayList<Integer>();

				// Initialize adjacencies
				for(int a = 0; a < vertexCount; a++){
					adjacencies.add(0);
				}
			}
			else{
				adjacencies = adjacencyMatrix[j - 1];
			}

			// Set Adjacency (add edge on the matrix)
			adjacencies.set(i - 1, 1);
			adjacencyMatrix[j - 1] = adjacencies;

			/**
			 * End mirroring
			 */
		}
	}

	/**
	 * Add access points of the vertex on the matrix 
	 * @param i - vertex
	 * @param focos - vector of access points
	 */
	public void addAccess(int i, int[] focos) {
		if (i >= 0 && i < vertexCount) {
			List<Integer> focosVertex = new ArrayList<Integer>();

			//Initialize
			for(int j = 0; j < accessCount; j++){
				focosVertex.add(0);
			}

			// Add all access points of the vertex
			for(int j = 0; j < focos.length; j++){
				focosVertex.set(focos[j] - 1, 1);
			}

			accessMatrix[i] = focosVertex;
		}
	}

	/**
	 * Format Adjacency Matrix 
	 * @return matrix of adjacencies on string format
	 */
	public String[] stringAdjMatrix(){
		String[] matrix = new String[vertexCount];
		String linha = "";
		
		for(int i=0; i<vertexCount; i++)
		{
			for(Integer valor: adjacencyMatrix[i]){
				linha += valor+" ";
			}
			matrix[i] = linha;
			linha = "";
		}
		return matrix;
	}

	/**
	 * Format Access Matrix 
	 * @return matrix of access points on string format
	 */
	public String[] stringAccessMatrix(){

		String[] matrix = new String[vertexCount];
		String linha = "";

		for(int i=0; i<vertexCount; i++)
		{
			for(Integer valor: accessMatrix[i]){
				linha += valor+" ";
			}
			matrix[i] = linha;
			linha = "";
		}
		return matrix;
	}

	/**
	 * Read Graph structure of file 
	 * @param path - file path
	 * @return Graph Class instance
	 * @throws IOException
	 */
	public static Graph readGraphIn(String path) throws IOException{

		int indice = -1;
		String[]valores;
		boolean edgesFineshed = false;
		int v1, v2, numVertices;
		int[]access = null;
		Graph graph = null;

		try { 

			FileReader arq = new FileReader(path); 
			BufferedReader lerArq = new BufferedReader(arq); 
			String linha = lerArq.readLine(); 

			while (linha != null) { 
				valores = linha.split(" ");

				if(indice == -1){
					// the first line of the file - vertices and edges size (m and n values)
					if(!edgesFineshed){
						numVertices = Integer.parseInt(valores[0]);
						graph = new Graph(numVertices);
						graph.vertexCount = Integer.parseInt(valores[0]);
						graph.edgesCount = Integer.parseInt(valores[1]);
					}
					else{
						// First line of access points -access points size (r value)
						graph.accessCount = Integer.parseInt(valores[0]);
					}
				}
				else{
					// If still are edges to read
					if(!edgesFineshed){
						v1 = Integer.parseInt(valores[0]);
						v2 = Integer.parseInt(valores[1]);

						graph.addEdge(v1, v2);
					}
					// If is reading Acess Points
					else{
						access = new int[valores.length];
						for(int i = 0; i < valores.length; i++){
							access[i] = Integer.parseInt(valores[i]);
						}
						graph.addAccess(indice, access);
					}
				}

				linha = lerArq.readLine(); 
				indice ++;

				// If all the edges were read (it means that will start reading the access points)
				if(indice > graph.edgesCount - 1){
					indice = -1;
					edgesFineshed = true;
				}
			} 
			arq.close(); 
		} catch (IOException e) { 
			System.err.printf("Erro na abertura do arquivo: %s.\n", e.getMessage()); 
		} 
		return graph;
	}

	/**
	 * Save Graph output on file
	 * @param path - output file path
	 * @param graph - Graph Class instance
	 * @throws IOException
	 */
	public static void saveGraphOut(String path, Graph graph) throws IOException{

		FileWriter arq = new FileWriter(path); 
		PrintWriter gravarArq = new PrintWriter(arq); 

		String[] stringAdjMatriz = graph.stringAdjMatrix();
		String[] stringAccessMatriz = graph.stringAccessMatrix();

		// Save adjacencyMatrix
		for (int i=0; i<stringAdjMatriz.length; i++) { 
			gravarArq.println(stringAdjMatriz[i]); 
		} 

		// Save accesMatrix
		for (int i=0; i<stringAccessMatriz.length; i++) { 
			gravarArq.println(stringAccessMatriz[i]); 
		}

		arq.close(); 
		System.out.println("Arquivo "+path+" salvo com sucesso!!!");
	}

	public static void main(String[] args) {

		if(args.length == 2){
			
			String dir = System.getProperty("user.dir");
	
			Scanner s = new Scanner(System.in);
	
			String pathIn = dir+"\\"+args[0];
	
			Graph g = new Graph(); 
			
			try {
				g = readGraphIn(pathIn);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String pathOut = dir+"\\"+args[1];
			
			try {
				saveGraphOut(pathOut, g);
			} catch (IOException e) {
				e.printStackTrace();
			}
	
			s.close();
		}
		else{
			System.out.println("Parâmetros Incorretos!");
		}
	}
}