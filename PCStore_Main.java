import java.sql.DriverManager;
import javax.swing.JOptionPane;

public class PCStore_Main {
	public static void main(String args[]) {
		
		String id = JOptionPane.showInputDialog("Oracle 아이디를 입력해주세요");
		String pw = JOptionPane.showInputDialog("Oracle 비밀번호를 입력해주세요");
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			PCStore_GUI.conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",
					id, pw);
			
			JOptionPane.showMessageDialog(null, "로그인 되었습니다.");
			PCStore_GUI gui = new PCStore_GUI();
			gui.setVisible(true);
			
		}catch(Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "로그인 할 수가 없습니다. 아이디와 비밀번호를 다시 입력해주세요.");
		}
	}
}