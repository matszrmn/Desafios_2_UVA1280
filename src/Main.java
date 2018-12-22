import java.util.Scanner;
import java.util.LinkedList;
import java.util.Map;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Main {
	public static LinkedList<Double> coeficientes = null;
	public static Map<Integer, Double> coeficientesAoQuadrado = null;
	public static LinkedList<Double> marcas = null;
	public static int grauPolinomio = 0;

	public static String arredondar(double numero) {
		DecimalFormat df = new DecimalFormat("#.##");
		String resp = String.valueOf(Double.parseDouble(df.format(numero).replace(",", ".")));
		if(resp.indexOf(".")+2 >= resp.length()) {
			resp += "0";
		}
		return resp;
	}
	@SuppressWarnings("unchecked")
	public static void preencherPolinomioAoQuadrado() {
		int grau1 = 0;
		int grau2 = 0;
		Double coefMap;
		
		LinkedList<Double> coeficientesCopia = (LinkedList<Double>)coeficientes.clone();
		
		for(Double coefAtual1 : coeficientes) {
			grau2 = 0;
			for(Double coefAtual2 : coeficientesCopia) {
				coefMap = coeficientesAoQuadrado.get(grau1 + grau2);
				
				if(coefMap == null) coeficientesAoQuadrado.put(grau1 + grau2, coefAtual1 * coefAtual2);
				else coeficientesAoQuadrado.put(grau1 + grau2, (coefAtual1 * coefAtual2) + coefMap);
				grau2++;
			}
			grau1++;
		}
	}
	public static double valorPolinomioAoQuadrado(double x) {
		double aux;
		double total = 0;
		
		total += coeficientesAoQuadrado.get(0);
		for(int i=1; i<=2*grauPolinomio; i++) {
			aux = Math.pow(x, i);
			aux = aux * coeficientesAoQuadrado.get(i);
			total += aux;
		}
		return total;
	}
	public static double volume(double esq, double dir, int n){
		if(n%2==1) n++;     
		
		double h = (dir - esq)/(double)n;
		double soma = valorPolinomioAoQuadrado(esq) + valorPolinomioAoQuadrado(dir);
		
		for(int i=1; i<n; i+=2){
			soma += 4*valorPolinomioAoQuadrado(esq + (i*h));
		}
		for(int i=2; i<n; i+=2){
			soma += 2*valorPolinomioAoQuadrado(esq + (i*h));
		}
		return (soma * h/3) * Math.PI;
	}
	public static double buscarMarcaDoVolume(double esq, double dir, double volume) {
		double xLow = esq;
		double med;
		double volAux;
		
		while(esq < dir){
			med = (esq + dir) / 2;
			volAux = volume(xLow, med, 1000);

			if(volume >= volAux) esq = med + 0.000001;
			else dir = med - 0.000001;
		}
		if(Math.abs(volume(xLow, esq, 1000) - volume) < 0.01) return esq;
		return -1;
	}
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		
		String coeficientesLinha;
		String lowHighPart;
		
		double xLow;
		double xHigh;
		double distanciaX;
		double volumeParticoes;
		double volumePartidoAcumulado;
		
		double volumeTotal;
		
		int caso = 1;
		int quantidadeMarcas;
		int ultimaIteracao;
		int iterator;
		
		while(sc.hasNext()) {
			coeficientes = new LinkedList<Double>();
			coeficientesAoQuadrado = new HashMap<Integer, Double>();
			marcas = new LinkedList<Double>();
			
			grauPolinomio = Integer.parseInt(sc.nextLine());
			coeficientesLinha = sc.nextLine();
			lowHighPart = sc.nextLine();
			
			for(int i=0; i<=grauPolinomio; i++) {
				if(i!=grauPolinomio){
					coeficientes.add(Double.parseDouble(coeficientesLinha.substring(0, coeficientesLinha.indexOf(" "))));
					coeficientesLinha = coeficientesLinha.substring(coeficientesLinha.indexOf(" ")+1);
				}
				else coeficientes.add(Double.parseDouble(coeficientesLinha));
			}
			xLow = Double.parseDouble(lowHighPart.substring(0, lowHighPart.indexOf(" ")));
			lowHighPart = lowHighPart.substring(lowHighPart.indexOf(" ")+1);
			xHigh = Double.parseDouble(lowHighPart.substring(0, lowHighPart.indexOf(" ")));
			lowHighPart = lowHighPart.substring(lowHighPart.indexOf(" ")+1);
			volumeParticoes = Double.parseDouble(lowHighPart);
			
			
			preencherPolinomioAoQuadrado();
			coeficientes = null;
			volumeTotal = volume(xLow, xHigh, 1000);
			System.out.println("Case " + caso + ": " + arredondar(volumeTotal));
			
			volumePartidoAcumulado = volumeParticoes;
			int pedacos = 0;
			
			while(volumePartidoAcumulado < volumeTotal && pedacos<8) {
				pedacos++;
				distanciaX = buscarMarcaDoVolume(xLow, xHigh, volumePartidoAcumulado) - xLow;
				if(distanciaX != -1) marcas.add(distanciaX);
				volumePartidoAcumulado += volumeParticoes;
			}
			
			quantidadeMarcas = marcas.size();
			if(quantidadeMarcas > 0) {
				ultimaIteracao = quantidadeMarcas-1;
				iterator = 0;
				
				for(Double marca : marcas) {
					if(iterator != 7 && iterator != ultimaIteracao) System.out.print(arredondar(marca)+" ");
					else {
						System.out.println(arredondar(marca));
						break;
					}
					iterator++;
				}
			}
			else System.out.println("insufficient volume");
			caso++;
		}
		sc.close();
	}
}
