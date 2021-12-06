import java.nio.ByteBuffer;
import java.util.Arrays;

public class dirtyTest {

	public static void main(String[] args) {
		ByteBuffer buffer = ByteBuffer.allocate(DBParams.pageSize);
		for(int i =0; i<10; i++) {
			PageId id = DiskManager.AllocPage();
			System.out.println(id);
			DiskManager.writePage(id, buffer);
		}
		
	}

}
