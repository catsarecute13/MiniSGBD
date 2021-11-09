
public class ListeChainee {
	public int index; 
	public ListeChainee framePre; 
	public ListeChainee frameSuiv; 
	
	public ListeChainee() {
	}
	
	public ListeChainee (int index) {
		this.index = index; 
		framePre = null; 
		frameSuiv = null; 	
	}
	
	public void supprimer() {
		framePre.frameSuiv = this.frameSuiv; 
		if(frameSuiv !=null) {
			frameSuiv.framePre = this.framePre;
		}
		
	}
	
	public void ajouter(ListeChainee frame) {
		frame.frameSuiv = null; 
		frame.framePre = this; 
		frameSuiv = frame; 
	}
}
