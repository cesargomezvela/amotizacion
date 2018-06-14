package amortizacion;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AmortizationTable {
	
	private static boolean debug=false;
	
	class EvalCreditBean{
		double totalIntereses;
		double montoCredito;
		int ciclo;
		double capitalRemanente;
		double intereseRemanente;
		public double getTotalIntereses() {
			return totalIntereses;
		}
		public void setTotalIntereses(double totalIntereses) {
			this.totalIntereses = totalIntereses;
		}
		public double getMontoCredito() {
			return montoCredito;
		}
		public void setMontoCredito(double montoCredito) {
			this.montoCredito = montoCredito;
		}
		public int getCiclo() {
			return ciclo;
		}
		public void setCiclo(int ciclo) {
			this.ciclo = ciclo;
		}
		public double getCapitalRemanente() {
			return capitalRemanente;
		}
		public void setCapitalRemanente(double capitalRemanente) {
			this.capitalRemanente = capitalRemanente;
		}
		public double getIntereseRemanente() {
			return intereseRemanente;
		}
		public void setIntereseRemanente(double intereseRemanente) {
			this.intereseRemanente = intereseRemanente;
		}
		@Override
		public String toString() {
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			return "Monto del credito: "+nf.format(montoCredito)+ " Total de intereces del credito: "+nf.format(totalIntereses)+"\n "
					+ "Remanente desde el ciclo "+ciclo+" Capital: "+nf.format(capitalRemanente) +" Intereses: "+nf.format(intereseRemanente);
		}
		
	}
	
	class CreditBean {
		double monto;
		int plazo;
		double interes;
		int pagosRealizados;
		
		public CreditBean(double monto, int plazo, double interes, int pagosRealizados) {
			super();
			this.monto = monto;
			this.plazo = plazo;
			this.interes = interes;
			this.pagosRealizados = pagosRealizados;
		}
		public double getMonto() {
			return monto;
		}
		public void setMonto(double monto) {
			this.monto = monto;
		}
		public int getPlazo() {
			return plazo;
		}
		public void setPlazo(int plazo) {
			this.plazo = plazo;
		}
		public double getInteres() {
			return interes;
		}
		public void setInteres(double interes) {
			this.interes = interes;
		}
		public int getPagosRealizados() {
			return pagosRealizados;
		}
		public void setPagosRealizados(int pagosRealizados) {
			this.pagosRealizados = pagosRealizados;
		}
		
		
	}
	
	public  EvalCreditBean calculaCredito(CreditBean credit ) {
		int siceCicle = credit.getPagosRealizados();
		siceCicle++;

		double principal = credit.getMonto();
		int length = credit.getPlazo();
		double interest = credit.getInteres();

		double monthlyInterest = interest / (12 * 100);
		double monthlyPayment = principal * (monthlyInterest / (1 - Math.pow((1 + monthlyInterest), (length * -1))));

		final int PAYMENT_WIDTH = 15;
		final int AMOUNT_WIDTH = 15;
		final int PRINCIPAL_WIDTH = 15;
		final int INTEREST_WIDTH = 15;
		final int BALANCE_WIDTH = 15;

		String pattern = "%" + PAYMENT_WIDTH + "s%" + AMOUNT_WIDTH + "s%" + PRINCIPAL_WIDTH + "s%" + INTEREST_WIDTH
				+ "s%" + BALANCE_WIDTH + "s";
		
		if(debug) {
			System.out.printf(pattern, "PAYMENT", "AMOUNT", "PRINCIPAL", "INTEREST", "BALANCE");
			System.out.println();
		}

		NumberFormat nf = NumberFormat.getCurrencyInstance();

		double totalInterest = 0;
		double interesSince = 0;
		double remainingCapitalSince = 0;
		for (int x = 1; x <= length; x++) {
			double amountInterest = principal * monthlyInterest;
			double amountPrincipal = monthlyPayment - amountInterest;
			principal = principal - amountPrincipal;
			totalInterest += amountInterest;
			if(x>=siceCicle) {
				interesSince += amountInterest;
			}
			if(x==siceCicle) {
				remainingCapitalSince = principal+amountPrincipal;
			}
			if(debug) {
				System.out.printf(pattern, x, nf.format(monthlyPayment), nf.format(amountPrincipal),
						nf.format(amountInterest), nf.format(principal));
				System.out.println();
			}

		}
		EvalCreditBean evalCreditoBean = new EvalCreditBean();
		evalCreditoBean.setCapitalRemanente(remainingCapitalSince);
		evalCreditoBean.setCiclo(siceCicle);
		evalCreditoBean.setIntereseRemanente(interesSince);
		evalCreditoBean.setMontoCredito(credit.getMonto());
		evalCreditoBean.setTotalIntereses(totalInterest);
		return evalCreditoBean;
	}

	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.US);
		AmortizationTable at = new AmortizationTable();
		CreditBean credit1 = at.new CreditBean(250, 48, 28.5, 5);
		CreditBean credit2 = at.new CreditBean(250, 48, 35, 5);
		CreditBean credit3 = at.new CreditBean(250, 24, 34, 5);
		CreditBean credit4 = at.new CreditBean(250, 48, 19.5, 5);
		CreditBean credit5 = at.new CreditBean(250, 12, 25, 5);
		CreditBean credit6 = at.new CreditBean(250, 48, 33, 5);
		CreditBean credit7 = at.new CreditBean(250, 12, 27, 5);
		CreditBean credit8 = at.new CreditBean(250, 24, 32, 5);
		CreditBean credit9 = at.new CreditBean(250, 24, 34, 5);
		CreditBean credit10 = at.new CreditBean(250, 18, 33.5, 6);
		CreditBean credit11 = at.new CreditBean(250, 12, 47, 6);
		CreditBean credit12 = at.new CreditBean(250, 48, 40.5, 5);
		
		List<CreditBean> credits = Arrays.asList(
				credit1,
				credit2,
				credit3,
				credit4,
				credit5,
				credit6,
				credit7,
				credit8,
				credit9,
				credit10,
				credit11,
				credit12);
		
		double montoPortafolio = 0;
		double interesesTotPortafolio=0;
		double capitalRemanentePortaFolio=0;
		double interesesRemanentesPortafolio=0;
		
		for (CreditBean creditBean : credits) {
			EvalCreditBean evalBean = at.calculaCredito(creditBean);
			montoPortafolio+=evalBean.getMontoCredito();
			interesesTotPortafolio+=evalBean.getTotalIntereses();
			capitalRemanentePortaFolio+=evalBean.getCapitalRemanente();
			interesesRemanentesPortafolio+=evalBean.getIntereseRemanente();
			if(debug) {
				System.out.println(evalBean);
			}
		}

		NumberFormat nf = NumberFormat.getCurrencyInstance();
		System.out.println("Monto del portafolio: "+nf.format(montoPortafolio)+ " Total de intereces del portafolio: "+nf.format(interesesTotPortafolio)+"\n "
					+ "Remanente Capital: "+nf.format(capitalRemanentePortaFolio) +" Intereses: "+nf.format(interesesRemanentesPortafolio));
		
		
	}
}
