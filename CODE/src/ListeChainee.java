
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
		frame.frameSuiv = this.frameSuiv; 
		frame.framePre = this; 
		frameSuiv = frame;
		if(frame.frameSuiv !=null) {
			frame.frameSuiv.framePre=frame; 			
		}
	}
	
	public String toString() {
		return "(index :"+ index+" | frameSuiv :"+ (frameSuiv == null ? "null" : frameSuiv.toString()+")");
	}
}
