package warmUp;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Graph {

	//	private int adjacencyMatrix[][];
	private List<Integer>[] acessMatrix, adjacencyMatrix;
	private int vertexCount, acessCount, edgesCount;

	public Graph(){

	}

	@SuppressWarnings("unchecked")
	public Graph(int vertexCount) {
		this.vertexCount = vertexCount;
		adjacencyMatrix = new ArrayList[vertexCount];//new int[vertexCount][vertexCount];
		acessMatrix = new ArrayList[vertexCount]; 
	}

	//	public void addEdge(int i, int j) {
	//		if (i > 0 && i <= vertexCount && j > 0 && j <= vertexCount) {
	//			adjacencyMatrix[i-1][j-1] = 1;
	//			adjacencyMatrix[j-1][i-1] = 1;
	//		}
	//	}

	public void addEdge(int i, int j) {
		if (i > 0 && i <= vertexCount) {
			List<Integer> adjacencies = new ArrayList<Integer>();
			
			if(adjacencyMatrix[i - 1] == null){
				
				adjacencies = new ArrayList<Integer>();

				for(int a = 0; a < vertexCount; a++){
					adjacencies.add(0);
				}
			}
			else{
				adjacencies = adjacencyMatrix[i - 1];
			}

			adjacencies.set(j - 1, 1);
			adjacencyMatrix[i - 1] = adjacencies;

			if(adjacencyMatrix[j - 1] == null){
				
				adjacencies = new ArrayList<Integer>();

				for(int a = 0; a < vertexCount; a++){
					adjacencies.add(0);
				}
			}
			else{
				adjacencies = adjacencyMatrix[j - 1];
			}

			adjacencies.set(i - 1, 1);
			adjacencyMatrix[j - 1] = adjacencies;

		}
	}

	public void addAcess(int i, int[] focos) {
		if (i >= 0 && i < vertexCount) {
			List<Integer> focosVertex = new ArrayList<Integer>();

			for(int j = 0; j < acessCount; j++){
				focosVertex.add(0);
			}

			for(int j = 0; j < focos.length; j++){
				focosVertex.set(focos[j] - 1, 1);
			}

			acessMatrix[i] = focosVertex;
		}
	}

	//	public void removeEdge(int i, int j) {
	//		if (i >= 0 && i < vertexCount && j > 0 && j < vertexCount) {
	//			adjacencyMatrix[i-1][j-1] = 0;
	//			adjacencyMatrix[j-1][i-1] = 0;
	//		}
	//	}
	//
	//	public int isEdge(int i, int j) {
	//		if (i >= 0 && i < vertexCount && j > 0 && j < vertexCount)
	//			return adjacencyMatrix[i][j];
	//		else
	//			return 0;
	//	}

	public void printAdjMatrix(){
		for(int i=0; i<vertexCount; i++)
		{
			for(Integer valor: adjacencyMatrix[i]){
				System.out.print(valor+" ");
			}

			System.out.println();
		}
	}

	public void printAcessMatrix(){
		for(int i=0; i<vertexCount; i++)
		{
			for(Integer valor: acessMatrix[i]){
				System.out.print(valor+" ");
			}

			System.out.println();
		}
	}

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

	public String[] stringAcessMatrix(){

		String[] matrix = new String[vertexCount];
		String linha = "";

		for(int i=0; i<vertexCount; i++)
		{
			for(Integer valor: acessMatrix[i]){
				linha += valor+" ";
			}
			matrix[i] = linha;
			linha = "";
		}
		return matrix;
	}

	public static Graph readGraph(String endereco) throws IOException{

		int indice = -1;
		String[]valores;
		boolean edgesFineshed = false;
		int v1, v2, numVertices;
		int[]acess = null;
		Graph graph = null;

		try { 

			FileReader arq = new FileReader(endereco); 
			BufferedReader lerArq = new BufferedReader(arq); 
			String linha = lerArq.readLine(); 

			while (linha != null) { 
				valores = linha.split(" ");

				if(indice == -1){
					if(!edgesFineshed){
						numVertices = Integer.parseInt(valores[0]);
						graph = new Graph(numVertices);
						graph.vertexCount = Integer.parseInt(valores[0]);
						graph.edgesCount = Integer.parseInt(valores[1]);
					}
					else{
						graph.acessCount = Integer.parseInt(valores[0]);
					}
				}
				else{
					if(!edgesFineshed){
						v1 = Integer.parseInt(valores[0]);
						v2 = Integer.parseInt(valores[1]);

						graph.addEdge(v1, v2);
					}
					else{
						acess = new int[valores.length];
						for(int i = 0; i < valores.length; i++){
							acess[i] = Integer.parseInt(valores[i]);
						}
						graph.addAcess(indice, acess);
					}
				}

				linha = lerArq.readLine(); 
				indice ++;

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

	public static void salvarArquivo(String endereco, Graph graph) throws IOException{

		FileWriter arq = new FileWriter(endereco); 
		PrintWriter gravarArq = new PrintWriter(arq); 

		String[] stringAdjMatriz = graph.stringAdjMatrix();
		String[] stringAcessMatriz = graph.stringAcessMatrix();

		for (int i=0; i<stringAdjMatriz.length; i++) { 
			gravarArq.println(stringAdjMatriz[i]); 
		} 

		for (int i=0; i<stringAcessMatriz.length; i++) { 
			gravarArq.println(stringAcessMatriz[i]); 
		}

		arq.close(); 
		System.out.println("Arquivo "+endereco+" salvo com sucesso!!!");
	}

	public static void main(String[] args) {

		//		Path currentRelativePath = Paths.get("");
		//		String str = currentRelativePath.toAbsolutePath().toString();
		//		System.out.println("Current relative path is: " + str);

		String raiz = System.getProperty("user.dir");

		Scanner s = new Scanner(System.in);

		System.out.println("Digite o nome do arquivo de entrada e tecle ENTER:");
		String enderecoEntrada = raiz+"\\"+s.next();

		//"c:/graph.txt";
		Graph g = new Graph(); 
		try {
			g = readGraph(enderecoEntrada);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Digite o nome do arquivo de saída e tecle ENTER:");
		String enderecoSaida = raiz+"\\"+s.next();
		try {
			salvarArquivo(enderecoSaida, g);
		} catch (IOException e) {
			e.printStackTrace();
		}

		s.close();
	}
}
