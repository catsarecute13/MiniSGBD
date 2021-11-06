
import java.nio.ByteBuffer;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws Exception{
		PageId id=DiskManager.AllocPage();
		System.out.println(id.FileIdx+"   "+id.PageIdx);
		byte [] buff = new byte[4096];
		for (int i= 0; i <4096; i++) {
			buff[i] = '1';
		}
		DiskManager.writePage(id, ByteBuffer.wrap(buff));
		DBManager.getDBManager().Init();
		Scanner lectureClavier=new Scanner(System.in);
		String chaine=new String();
		while (true){
			System.out.println(">>");
			chaine=lectureClavier.nextLine();
			if (chaine.equals("EXIT")){
				DBManager.getDBManager().Finish();
				break;
			}
			DBManager.getDBManager().ProcessCommand(chaine);
		}
		lectureClavier.close();
	}

}
