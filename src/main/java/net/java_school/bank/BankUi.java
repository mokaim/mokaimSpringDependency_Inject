package net.java_school.bank;

import java.io.*; 
import java.util.ArrayList;
import java.util.StringTokenizer; 
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BankUi {
	
	public static final String ENTER = System.getProperty("line.separator"); 
	private String accountFile ;
	private Bank bank ;
	public void setAccountFile(String accountFile) { 
		this.accountFile = accountFile; 
	} 
	
	public void setBank(Bank bank) { 
		this.bank = bank; 
	} 
	
	private void readyAccount() throws Exception { 
		FileReader fr = null;
		try { fr = new FileReader(accountFile); 
		} catch (FileNotFoundException e) { 
			throw new FileNotFoundException(accountFile + "파일을 찾을 수 없습니다."); 
		} BufferedReader br = new BufferedReader(fr); String strAccount = null;
		
		try { 
			while ((strAccount = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(strAccount, "|");
				String accountNo = st.nextToken(); 
				String name = st.nextToken(); 
				long balance = Long.parseLong(st.nextToken());
				String kind = st.nextToken(); 
				if (kind.equals(Account.ACCOUNT_NORMAL)) { 
					bank.addAccount(name, accountNo, balance, Account.ACCOUNT_NORMAL);
				} else if (kind.equals(Account.ACCOUNT_MINUS)) { 
					bank.addAccount(name, accountNo, balance, Account.ACCOUNT_MINUS);
				}
			} 
		
		} 
		
		catch (NumberFormatException e) { throw new NumberFormatException("잔액을 숫자로 바꿀 수 없습니다.");
		
		} catch (IOException e) { 
			throw new IOException("IO 예외가 발생했습니다.");
		} finally {
			if(br != null) {
				try {
					br.close();
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			if(fr != null) {
				try {
					fr.close();
				}catch(Exception e) {
					
				}
			}
		}
		
	}
	
	public void startWork() throws Exception {
		
		
		readyAccount(); 
		String menu = null;
		do { System.out.println(" ** 메뉴를 선택하세요 ** ");
		System.out.println(" 1 *** 계좌등록 ");
		System.out.println(" 2 *** 계좌목록 ");
		System.out.println(" 3 *** 입금 "); 
		System.out.println(" 4 *** 출금 "); 
		System.out.println(" 5 *** 이체 "); 
		System.out.println(" q *** 종료 "); 
		System.out.println(" ********************** "); 
		
		System.out.println(">>");
		
		try { BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		menu = br.readLine();
		if (menu.equals("1")) { 
			 System.out.println("이름을 입력하세요.>>");
			 String name = br.readLine();
			 System.out.println("계좌번호를 입력하세요.>>");
			 String accountNo = br.readLine();
			 System.out.println("일반계좌는 1,마이너스계좌는 2를 입력하세요.>>");
			 String kind = br.readLine(); 
			 if (kind.equals("1")) {
				 bank.addAccount(name, accountNo, Account.ACCOUNT_NORMAL);
			 } else if (kind.equals("2")) {
				 bank.addAccount(name, accountNo, Account.ACCOUNT_MINUS);
			 } else {
				 System.out.println("계좌번호를 잘못 입력했습니다."); 
			  } 
		} else if ( menu.equals("2") ) {
			 ArrayList<Account> accounts = bank.getAccounts(); 
			 int totalAccount = accounts.size();
			 
			 for ( int i = 0; i < totalAccount; i++ ) { 
				 System.out.println(accounts.get(i)); 
			  
			 }
			 
		  } else if (menu.equals("3")) { 
			  System.out.println("계좌번호를 입력하세요.>>"); 
			  String accountNo = br.readLine(); 
			  Account account = bank.findAccount(accountNo); 
			  if (account != null) { 
				  System.out.println("입금액을 입력하세요.>>");
				  long money = Long.parseLong(br.readLine()); 
				  account.deposit(money); 
				  
			   } else {
				   System.out.println("계좌번호를 잘못 입력했습니다.");
			     } 
			} else if (menu.equals("4")) { 
				    System.out.println("계좌번호를 입력하세요.>>");
				    String accountNo = br.readLine();
				    Account account = bank.findAccount(accountNo); 
				    if (account != null) { System.out.println("출금액을 입력하세요.>>"); 
				    long money = Long.parseLong(br.readLine()); 
				    account.withdraw(money); 
			   } else { System.out.println("계좌번호를 잘못 입력했습니다."); } 
		    } else if (menu.equals("5")) {
		    	 System.out.println("FROM 계좌에서 TO 계좌로 송금합니다.");
		    	 System.out.println("FROM 계좌번호를 입력하세요>>"); 
		    	 String accountNo = br.readLine();
		    	 Account from = bank.findAccount(accountNo);
		    	 System.out.println("TO 계좌번호를 입력하세요.>>");
		    	 accountNo = br.readLine(); Account to = bank.findAccount(accountNo);
		    	 if (from != null && to != null) { 
		    		 System.out.println("이체금액을 입력하세요.>>");
		    		 long money = Long.parseLong(br.readLine());
		    		 from.withdraw(money); to.deposit(money); 
		         } else { 
		        	 System.out.println("계좌번호를 잘못 입력하여 이체가 진행되지 않았습니다.");
		           } 
		      }
		  }catch(Exception e) {
			  e.printStackTrace();
		  }
		}while(!menu.equals('q'));
		
		endWork();
		
	}
	
	private void endWork() { 
		StringBuilder sb = new StringBuilder();
		ArrayList<Account> accounts = bank.getAccounts();
		int totalAccount = accounts.size(); 
		for (int i = 0; i < totalAccount; i++) { 
			sb.append(accounts.get(i)); sb.append(ENTER); 
			
		} 
		
		FileWriter fw = null; 
		BufferedWriter bw = null;
		try { 
			fw = new FileWriter(accountFile , false); 
			bw = new BufferedWriter(fw); 
			bw.write(sb.toString()); 
			
		} catch (IOException e) { e.printStackTrace(); 
		
		} finally { 
			if (bw != null) { 
				try { 
					bw.close(); 
				
				} catch (IOException e) {
					e.printStackTrace(); 
					
				} 
				
			} if (fw != null) {
				try {
					fw.close(); 
					
				} catch (IOException e) { 
					e.printStackTrace(); 
					
				} 
				
			} 
			
		} 
		
	} 
	
	public static void main(String[] args) { 
		@SuppressWarnings("resource")
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		BankUi ui = (BankUi)ctx.getBean("bankUi");
		try { 
			ui.startWork();
			
		} catch (Exception e) { 
			System.out.println(e.getMessage()); 
			
		} 
		
		
		System.out.println("프로그램이 종료되었습니다.");
	}
		
	

}


