public class Main {

	public static void main(String[] args) throws Exception{
		PageId id=DiskManager.AllocPage();
		System.out.println(id.FileIdx+"   "+id.PageIdx);
		byte [] buff = new byte[4096];
		for (int i= 0; i <4096; i++) {
			buff[i] = '1';
		}
		DiskManager.writePage(id, buff);
		
	}

}
