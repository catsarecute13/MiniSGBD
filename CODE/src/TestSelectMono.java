
public class TestSelectMono {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String ch="SELECTMONO * FROM FHGD WHERE nomColOPValeur AND nomCokzehyifhe"; 
		SelectMonoCommand select = new SelectMonoCommand(ch); 
		System.out.println(select.verifWhere); 
		System.out.println("Nom relation : " +select.nomRelation); 
		System.out.println(select.conditions); 
		
	}

}
