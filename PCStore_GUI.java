import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

public class PCStore_GUI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	static Connection conn = null;
	static PreparedStatement stmt = null;
	
	JPanel MainPanel; // 메인패널
	JTabbedPane TabbedInfoPanel; // 탭패널
	JPanel BuyPanel; // 구매패널
	
	JLabel TabSearchLabel = new JLabel();
	JLabel TabBuyLabel = new JLabel();
	JLabel TabBuyListLabel = new JLabel();
	JLabel PCModelLabel = new JLabel();
	JLabel LaptopModelLabel = new JLabel();
	JLabel PrinterModelLabel = new JLabel();
	JLabel TotalIncome = new JLabel();
	
	JTextField PCmodelInput = new JTextField();
	JTextField LaptopmodelInput = new JTextField();
	JTextField PrintermodelInput = new JTextField();
	
	JButton BuyButton = new JButton("구매");
	JButton CloseButton = new JButton("닫기");
	JButton FinalBuyButton = new JButton("최종 구매");
	JButton ResetButton = new JButton("리셋");
	JButton InquiryButton = new JButton("조회");
	
	JComboBox<String> search = new JComboBox<String>();
	
	JComboBox<Integer> PCCombo = new JComboBox<Integer>();
	JComboBox<Integer> LaptopCombo = new JComboBox<Integer>();
	JComboBox<Integer> PrinterCombo = new JComboBox<Integer>();
	
	static JTextArea TapSearchTextArea;
	static JTextArea TapFinalBuyTextArea;
	static JTextArea TapBuyListTextArea;
	
	int TotPrice = 0;
	int PCPrice = 0;
	int LaptopPrice = 0;
	int PrinterPrice = 0;
	
	String PCModel = "";
	String LaptopModel = "";
	String PrinterModel = "";
	String TotIncome = ""; // 총 수입
	
	int PCCnt = 0;
	int LaptopCnt = 0;
	int PrinterCnt = 0;
	
	public PCStore_GUI() {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		setTitle("JDBC와 자바 GUI 실습"); // 상단 타이틀바
		setBounds(100, 20, 930, 440); // 전체 크기
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		MainPanel = new JPanel(); // 메인 패널 생성
		MainPanel.setLayout(null);
		MainPanel.setBounds(100,100,800,440);
		
		makeComponent(); // makeComponent 호출
		
		getContentPane().add(MainPanel, BorderLayout.CENTER);
	}
	
	public void makeComponent() {
		// --------------- 구매 패널 -------------
		BuyPanel = new JPanel();
		BuyPanel.setLayout(null);
		BuyPanel.setFont(new Font("필기체", 0, 12));
		BuyPanel.setBorder(new TitledBorder("구매"));
		BuyPanel.setBounds(10, 85, 360, 300);
		
		MainPanel.add(BuyPanel, BorderLayout.CENTER);
		PCmodelInput.setBounds(112, 71, 70, 30);
		BuyPanel.add(PCmodelInput);
		LaptopmodelInput.setBounds(112, 121, 70, 30);
		BuyPanel.add(LaptopmodelInput);
		PrintermodelInput.setBounds(112, 171, 70, 30);
		BuyPanel.add(PrintermodelInput);
		
		// 모델 번호, 개수 라벨
		JLabel modelLabel = new JLabel();
		modelLabel.setText("모델 번호");
		modelLabel.setBounds(122, 41, 54, 30);
		BuyPanel.add(modelLabel);
		
		JLabel countLabel = new JLabel();
		countLabel.setText("개수");
		countLabel.setBounds(252, 41, 29, 30);
		BuyPanel.add(countLabel);
		
		// 모델 라벨
		PCModelLabel.setBounds(112, 71, 70, 30);
		BuyPanel.add(PCModelLabel);
		
		LaptopModelLabel.setBounds(112, 121, 70, 30);
		BuyPanel.add(LaptopModelLabel);
		
		PrinterModelLabel.setBounds(112, 171, 70, 30);
		BuyPanel.add(PrinterModelLabel);
		
		JLabel PCLabel = new JLabel();
		PCLabel.setText("PC");
		PCLabel.setBounds(32, 72, 54, 30);
		BuyPanel.add(PCLabel);
		
		JLabel LaptopLabel = new JLabel();
		LaptopLabel.setText("Laptop");
		LaptopLabel.setBounds(32, 115, 70, 41);
		BuyPanel.add(LaptopLabel);
		
		JLabel PrinterLabel = new JLabel();
		PrinterLabel.setText("Printer");
		PrinterLabel.setBounds(32, 165, 70, 41);
		BuyPanel.add(PrinterLabel);
		
		// 모델 갯수 콤보박스
		PCCombo.setBounds(232, 71, 70, 30);
		for(int i = 1; i < 11; i++) {
			PCCombo.addItem(i);
		}
		BuyPanel.add(PCCombo);
		
		LaptopCombo.setBounds(232, 121, 70, 30);
		for(int i = 1; i < 11; i++) {
			LaptopCombo.addItem(i);
		}
		BuyPanel.add(LaptopCombo);
		
		PrinterCombo.setBounds(232, 171, 70, 30);
		for(int i = 1; i < 11; i++) {
			PrinterCombo.addItem(i);
		}
		BuyPanel.add(PrinterCombo);
		
		// 구매, 닫기 버튼
		BuyButton.setBounds(82, 237, 100, 35);
		BuyButton.addActionListener(this);
		BuyPanel.add(BuyButton);
		
		CloseButton.setBounds(202, 237, 100, 35);
		CloseButton.addActionListener(this);
		BuyPanel.add(CloseButton);
		
		MainPanel.add(BuyPanel); // 메인페널에 붙임
		// ----------------------------------------
		
		// ---------------상단 텍스트 패널-------------
		JPanel LeftTopPanel = new JPanel();
		LeftTopPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, null));
		LeftTopPanel.setBounds(10, 25, 360, 50);
		LeftTopPanel.setLayout(new BorderLayout());
		MainPanel.add(LeftTopPanel);
		
		JLabel Text1 = new JLabel();
		Text1.setFont(new Font("필기체", Font.BOLD, 22));
		Text1.setText("DB 컴퓨터가게");
		Text1.setHorizontalAlignment(JLabel.CENTER);
		LeftTopPanel.add(Text1);
		
		JPanel RightTopPanel = new JPanel();
		RightTopPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, null));
		RightTopPanel.setBounds(400, 25, 500, 50);
		RightTopPanel.setLayout(new BorderLayout());
		MainPanel.add(RightTopPanel);
		
		JLabel Text2 = new JLabel();
		Text2.setFont(new Font("필기체", Font.BOLD, 22));
		Text2.setText("좋은 시간 되세요. [2021년 / 12월 / 20일]");
		Text2.setHorizontalAlignment(JLabel.CENTER);		
		RightTopPanel.add(Text2);
		
		// ----------------------------------------
		// ----------------- 탭 패널 ----------------
		TabbedInfoPanel = new JTabbedPane();
		MainPanel.add(TabbedInfoPanel, BorderLayout.CENTER);
		JPanel TabbedInfoPanel_Search = new JPanel();
		JPanel TabbedInfoPanel_Buy = new JPanel();
		JPanel TabbedInfoPanel_BuyList = new JPanel();
		
		TabbedInfoPanel_Search.setLayout(null); // 패널의 Layout을 null : 사용자 지정을 위해
		TabbedInfoPanel_Buy.setLayout(null);
		TabbedInfoPanel_BuyList.setLayout(null);
		
		TabbedInfoPanel.addTab("조회", TabbedInfoPanel_Search); // 패널 붙임
		TabbedInfoPanel.addTab("최종구매", TabbedInfoPanel_Buy);
		TabbedInfoPanel.addTab("구매내역", TabbedInfoPanel_BuyList);
		TabbedInfoPanel.setBounds(400, 86, 500, 300); // 패널 크기
		MainPanel.add(TabbedInfoPanel); // 메인페널에 붙임
		// ----------------------------------------
		
		// -----------------Search 패널-------------
		TabSearchLabel = new JLabel();
		TabSearchLabel.setText("조회할 물품 선택");
		TabSearchLabel.setIcon(new ImageIcon(""));
		TabSearchLabel.setBounds(20, 0, 200, 80);
		TabbedInfoPanel_Search.add(TabSearchLabel);
		
		search = new JComboBox<String>();
		
		search.addItem("PC");
		search.addItem("Laptop");
		search.addItem("Printer");
		

		search.setBounds(150, 20, 130, 40);
		TabbedInfoPanel_Search.add(search);
		search.addActionListener(this);
		
		TapSearchTextArea = new JTextArea();
		TapSearchTextArea.setBorder(new LineBorder(Color.gray, 2));
		TapSearchTextArea.setEditable(false);
		
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(TapSearchTextArea);
		scroll.setBounds(20, 80, 450, 180);

		TabbedInfoPanel_Search.add(scroll);
		TabbedInfoPanel_Search.setVisible(true);
		
		
		// -------------- FinalBuy 패널 ----------------
		TapFinalBuyTextArea = new JTextArea();
		TapFinalBuyTextArea.setFont(new Font("필기체", 0, 12));
		TapFinalBuyTextArea.setForeground(Color.black);
		TapFinalBuyTextArea.setOpaque(true);
		TapFinalBuyTextArea.setBackground(Color.white);
		TapFinalBuyTextArea.setBounds(20, 15, 450, 200);
		TapFinalBuyTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, null));
		TapFinalBuyTextArea.setLineWrap(true);
		TapFinalBuyTextArea.setEditable(false);
		TabbedInfoPanel_Buy.add(TapFinalBuyTextArea);
		
		FinalBuyButton.setBounds(240, 225, 100, 35); // 최종구매 버튼
		FinalBuyButton.addActionListener(this);
		TabbedInfoPanel_Buy.add(FinalBuyButton);
		
		ResetButton.setBounds(360, 225, 100, 35); // 리셋 버튼
		ResetButton.addActionListener(this);
		TabbedInfoPanel_Buy.add(ResetButton);
		
		
		// --------------- BuyList 패널 ------------------
		TapBuyListTextArea = new JTextArea();
		TapBuyListTextArea.setFont(new Font("필기체", 0, 12));
		TapBuyListTextArea.setForeground(Color.black);
		TapBuyListTextArea.setOpaque(true);
		TapBuyListTextArea.setBackground(Color.white);
		TapBuyListTextArea.setBounds(20, 15, 450, 200);
		TapBuyListTextArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.BLACK, null));
		TapBuyListTextArea.setLineWrap(true);
		TapBuyListTextArea.setEditable(false);
		TabbedInfoPanel_BuyList.add(TapBuyListTextArea);
		
		InquiryButton.setBounds(360, 225, 100, 35); // 조회 버튼
		InquiryButton.addActionListener(this);
		TabbedInfoPanel_BuyList.add(InquiryButton);
		
		JLabel TotalIncomeText = new JLabel();
		TotalIncomeText.setText("KDE 컴퓨터 가게 총 수입 : ");
		TotalIncomeText.setBounds(25, 200, 200, 80);
		TabbedInfoPanel_BuyList.add(TotalIncomeText);
		
		TotalIncome.setText(null);
		TotalIncome.setBounds(180, 200, 200, 80);
		TabbedInfoPanel_BuyList.add(TotalIncome);
	}
	
	
	private void showTable() throws SQLException {
		String specification = "";
		
		String sqlStr = "SELECT count(column_name) num from cols where table_name = '" +
						((String)search.getSelectedItem()).toUpperCase() + "'";
		PreparedStatement stmt = conn.prepareStatement(sqlStr);
		ResultSet rs = stmt.executeQuery();
		
		rs.next();
		int number = rs.getInt("num");
		String[] tables = new String[number];
		
		sqlStr = "SELECT column_name from cols where table_name = '" +
				((String)search.getSelectedItem()).toUpperCase() + "'";
		stmt = conn.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for(number = 0; rs.next(); number++) {
			tables[number] = rs.getString("column_name");
			specification += tables[number] + "\t";
		}
		
		for(specification += '\n'; number > 0; number--) {
			specification += "---------------------";
		}
		
		specification += "\n";
		
		sqlStr = "SELECT * FROM " +	(String)search.getSelectedItem();
		stmt = conn.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for(number = 0; rs.next(); number++) {
			for(int i = 0; i < tables.length; i++) {
				specification += rs.getString(tables[i]) + "\t"; 
			}
			specification += '\n';
		}
		TapSearchTextArea.setText(specification);
		rs.close();
		stmt.close();
	}
	
	
	private void showFinalBuy() throws SQLException {
		String FinalBuyText = "";
		String sqlStr = "";
		
		if(!PCModel.isEmpty()) {
			sqlStr = "select * from pc where model=\'" + PCModel + "\'";
			PreparedStatement stmt = conn.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			PCPrice = rs.getInt("price");
			FinalBuyText += "-PC-" + '\n' + "PCmodel : " + PCModel + '\t' + "개수 : " + PCCnt + '\t'+ "가격 : $" + PCPrice*PCCnt + '\n' + '\n';
			rs.close();
			stmt.close();
		} else {
			FinalBuyText += "-PC-" + '\n' + "PCmodel : 없음" + '\t' +'\t' + "개수 : 0" + '\t'+ "가격 : $0" + '\n' + '\n';
		}
		
		if(!LaptopModel.isEmpty()) {
			sqlStr = "select * from laptop where model=\'" + LaptopModel + "\'";
			stmt = conn.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			LaptopPrice = rs.getInt("price");
			FinalBuyText += "-Laptop-" + '\n' + "Laptopmodel : " + LaptopModel + '\t' + "개수 : " + LaptopCnt + "\t"+ "가격 : $" + LaptopPrice*LaptopCnt + '\n' + '\n';
			rs.close();
			stmt.close();
		} else {
			FinalBuyText += "-Laptop-" + '\n' + "Laptopmodel : 없음 " + '\t' + "개수 : 0" + "\t"+ "가격 : $0" + '\n' + '\n';
		}
		
		if(!PrinterModel.isEmpty()) {
			sqlStr = "select * from printer where model=\'" + PrinterModel + "\'";
			stmt = conn.prepareStatement(sqlStr);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			PrinterPrice = rs.getInt("price");
			FinalBuyText += "-Printer-" + '\n' + "Printermodel : " + PrinterModel + '\t' + "개수 : " + PrinterCnt + "\t"+ "가격 : $" + PrinterPrice*PrinterCnt + '\n' + '\n';
			TapFinalBuyTextArea.setText(FinalBuyText);
			rs.close();
			stmt.close();
		} else {
			FinalBuyText += "-Printer-" + '\n' + "Printermodel : 없음 " + '\t' + "개수 : 0" + "\t"+ "가격 : $0" + '\n' + '\n';
		}
		TapFinalBuyTextArea.setText(FinalBuyText);
	}
	
	private void showBuyList() throws SQLException {
		String specification = "";
		
		String sqlStr = "SELECT count(column_name) num from cols where table_name = 'TRANSACTION'";
		PreparedStatement stmt = conn.prepareStatement(sqlStr);
		ResultSet rs = stmt.executeQuery();
		
		rs.next();
		int number = rs.getInt("num");
		String[] tables = new String[number];
		
		sqlStr = "SELECT column_name from cols where table_name = 'TRANSACTION'";
		stmt = conn.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for(number = 0; rs.next(); number++) {
			tables[number] = rs.getString("column_name");
			specification += tables[number] + "\t";
		}
		
		for(specification += '\n'; number > 0; number--) {
			specification += "--------------------";
		}
		
		specification += "\n";
		
		sqlStr = "SELECT * FROM transaction ORDER BY tnumber DESC";
		stmt = conn.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		
		for(number = 0; rs.next(); number++) {
			for(int i = 0; i < tables.length; i++) {
				specification += rs.getString(tables[i]) + "\t"; 
			}
			specification += '\n';
		}
		
		sqlStr = "SELECT sum(tprice) FROM transaction";
		stmt = conn.prepareStatement(sqlStr);
		rs = stmt.executeQuery();
		rs.next();
		TotIncome = rs.getString("sum(tprice)");
		TotalIncome.setText("$"+TotIncome); // 조회 버튼 누를시 총수입 표시
		TapBuyListTextArea.setText(specification);
		rs.close();
		stmt.close();
	}
	
	private void finalbuy() throws SQLException{
		TotPrice = 0; // 초기화
		if (!PCModel.isEmpty() && PCCnt != 0 && PCPrice != 0) {
			insertPC();
			TotPrice += PCPrice * PCCnt;
		}
		if (!LaptopModel.isEmpty() && LaptopCnt != 0 && LaptopPrice != 0) {
			insertLaptop();
			TotPrice += LaptopPrice * LaptopCnt;
		}
		if (!PrinterModel.isEmpty() && PrinterCnt != 0 && PrinterPrice != 0) {
			insertPrinter();
			TotPrice += PrinterPrice * PrinterCnt;
		}
	}
	
	private void insertPC() throws SQLException {
		String sqlStr = "INSERT into transaction (tnumber, tmodel, tcount, tprice)" + "VALUES(tnum_seq.NEXTVAL" + "," + PCModel 
				+ "," + PCCnt +"," + PCPrice*PCCnt + ")";
		stmt = conn.prepareStatement(sqlStr);
		stmt.executeUpdate();
		stmt.close();
	}
	
	private void insertLaptop() throws SQLException {
		String sqlStr = "INSERT into transaction (tnumber, tmodel, tcount, tprice)" + "VALUES(tnum_seq.NEXTVAL" + "," + LaptopModel 
				+ "," + LaptopCnt +"," + LaptopPrice*LaptopCnt + ")";
		stmt = conn.prepareStatement(sqlStr);
		stmt.executeUpdate();
		stmt.close();
	}
	
	private void insertPrinter() throws SQLException {
		String sqlStr = "INSERT into transaction (tnumber, tmodel, tcount, tprice)" + "VALUES(tnum_seq.NEXTVAL" + "," + PrinterModel 
				+ "," + PrinterCnt +"," + PrinterPrice*PrinterCnt + ")";
		stmt = conn.prepareStatement(sqlStr);
		stmt.executeUpdate();
		stmt.close();
	}
	
	private void reset() {
		PCmodelInput.setText(null); // 모델번호, 갯수 입력값 초기화
		LaptopmodelInput.setText(null);
		PrintermodelInput.setText(null);
		PCCombo.setSelectedIndex(0);
		LaptopCombo.setSelectedIndex(0);
		PrinterCombo.setSelectedIndex(0);
		
		PCModel = ""; // 변수값들 초기화
		LaptopModel = "";
		PrinterModel = "";
		
		PCCnt = 0;
		LaptopCnt = 0;
		PrinterCnt = 0;
		
		TotPrice = 0;
		PCPrice = 0;
		LaptopPrice = 0;
		PrinterPrice = 0;
	}
		
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == search) {
			try {
				showTable();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
		
		else if(e.getSource() == BuyButton) {
			PCModel = PCmodelInput.getText();
			LaptopModel = LaptopmodelInput.getText();
			PrinterModel = PrintermodelInput.getText();
			PCCnt = (int) PCCombo.getSelectedIndex() + 1;
			LaptopCnt = (int) LaptopCombo.getSelectedIndex() + 1;
			PrinterCnt = (int) PrinterCombo.getSelectedIndex() + 1;
			
			if(PCModel.isEmpty() && LaptopModel.isEmpty() && PrinterModel.isEmpty()) { // 아무것도 구매를 하지 않으면 메시지 출력
				JOptionPane.showMessageDialog(null, "세개 중에 한개는 입력해주세요.");
			}
			else {
				try {
					showFinalBuy();
					JOptionPane.showMessageDialog(null, "정상적으로 추가되었습니다.");
					PCmodelInput.setText(null); // 구매버튼을 누르면 모델번호, 갯수 입력값 초기화
					LaptopmodelInput.setText(null);
					PrintermodelInput.setText(null);
					PCCombo.setSelectedIndex(0);
					LaptopCombo.setSelectedIndex(0);
					PrinterCombo.setSelectedIndex(0);
				} catch(SQLException e1) {
					JOptionPane.showMessageDialog(null, "입력하신 모델이 존재하지 않습니다.");
					reset();
				}
			}
		}
		
		else if(e.getSource() == CloseButton) {
			JOptionPane.showMessageDialog(null, "프로그램을 종료합니다.", "메세지", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		}
		
		else if (e.getSource() == ResetButton) {
				TapFinalBuyTextArea.setText(""); // 구매패널 텍스트 문구 초기화
				reset(); 
				JOptionPane.showMessageDialog(null, "리셋되었습니다.");
		}

		else if(e.getSource() == FinalBuyButton) {
			if (PCCnt == 0 && LaptopCnt == 0 && PrinterCnt == 0) { // 구매패널에서 아무것도 하지 않은채, 최종 구매 누를시 메시지 출력
				JOptionPane.showMessageDialog(null, "먼저 구매를 해주세요.");
			}else {
				try {
					finalbuy();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "최종 구매하여 [총 금액 : $" + TotPrice + "]가 결제되었습니다.");
				int result = JOptionPane.showConfirmDialog(null, "계속 구매를 하시겠습니까?", "확인창", JOptionPane.YES_NO_OPTION);
				if(result == JOptionPane.NO_OPTION) { // 아니오를 눌렀을시 프로그램 종료
					JOptionPane.showMessageDialog(null, "프로그램을 종료합니다.");
					System.exit(0);
				} else {
						TapFinalBuyTextArea.setText(""); // 예를 눌렀을시 구매패널 초기화
						reset(); // 리셋
				}
			}
		}
	
		else if (e.getSource() == InquiryButton) {
				try {
					showBuyList();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

		}
	}
	
}
