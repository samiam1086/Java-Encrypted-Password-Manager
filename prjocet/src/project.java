import java.io.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.crypto.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.crypto.spec.*;
import org.apache.commons.codec.binary.Base64;

public class project {
	
	public static String ourDecrypt(String decryptme, int[] darray, String k, String iv){
		decryptme = aesDecrypt(decryptme, k, iv);
	    decryptme = decryptme.toLowerCase();
	    decryptme = decryptme.replaceAll("\\s+","");
	    int qq;
	    String zz = "";
	    for(int i = 0; i < decryptme.length(); i++){
	      qq = (int)(decryptme.charAt(i) - 'e');
	      zz = zz + Integer.toString(qq);
	    }
	    String kk = "";
	    int jj;
	    String cc = "";
	    for(int i = 0; i < zz.length() - 1; i += 2){
	      kk = zz.substring(i, i+2);
	      jj = Integer.valueOf(kk);
	      cc = cc + (char)jj;
	    }
	    decryptme = cc;
	    
	    // get the offset
	    int offset = 0;
	    String one = decryptme.substring(decryptme.length() - 7, decryptme.length()- 6);
	    String two = decryptme.substring(decryptme.length() - 6, decryptme.length() - 5);
	    String three = decryptme.substring(decryptme.length() - 5, decryptme.length() - 4);
	    offset = offset + (((Integer.valueOf(one)) * 100)) + ((Integer.valueOf(two)) * 10) + (Integer.valueOf(three));
	    // okay we got the offset now to group them into threes
	    
	    for(int i = 0; i <= decryptme.length()-3; i= i + 3){
	      int place = 0;
	      // get each of the 3 numbers into their own string and them get them to the correct value
	      String x = decryptme.substring(i, i+1);
	      String y = decryptme.substring(i+1,i+2);
	      String z = decryptme.substring(i+2,i+3);
	      place = place + ((Integer.valueOf(x)) * 100);
	      place = place + ((Integer.valueOf(y)) * 10);
	      place = place + Integer.valueOf(z);
	      // take the offset out of all the numbers
	      for(int j = 0; j <= 0; j++){
	        darray[i] = place - offset;
	      }  
	    }
	    
	    // turn the decimal text back into chars
	    String returner = "";
	    for(int co = 0; co <= darray.length-1; co++){
	      if(darray[co] > 0){
	        if(darray[co] == 206){ // the search for the marker
	          co = darray.length-1;
	        }else{
	          if(darray[co] == 218){
	            returner += " ";
	          }else{
	            returner += (char)darray[co];
	          }
	        }
	      }
	    }
	    return returner;
	  }
	  
	  public static String ourEncrypt(String encryptme, int offset, int salt, int salt1, int salt2, String k, String iv){
	    String encrypted = "";
	    // scan through the inputted string and if they have a space replace it with a nontypable char
	    for(int i = 0; i <= encryptme.length()-1; i++){
	      if(encryptme.charAt(i) == ' '){
	        encrypted = encrypted + (offset + 218);
	      }else{
	    	  // if the char isnt a space add the offset to it and add it to the string
	         encrypted = encrypted + (offset + (int)encryptme.charAt(i));
	      }
	    }
	        
	    int equal = offset + 206; // a marker that isnt a typable char
	    int underscore = offset + ((char)'_');// underscores for length
	    String x = encrypted + equal + underscore + underscore + salt + salt1 + salt2 + offset + salt1;
	    String q = "";
	    char r;
	    String o = "";
	    for(int i = 0; i < x.length(); i++){
	      int u = Integer.valueOf(x.charAt(i));
	      o = o + Integer.toString(u);
	    }
	    
	    for(int i = 0; i < o.length(); i++){
	      int l = (int)(Math.random() * 2);
	      String d = o.substring(i, i+1);
	      int e = Integer.valueOf(d);
	      r = (char)(e + 'e');
	      if(l >= 1){
	        r = Character.toUpperCase(r);
	      }
	      if((i % 4) == 0){
	         q = q + r + " ";
	      }else{
	         q = q + r;
	      }
	    }
	    q = aesEncrypt(q, k, iv);
	    return q;
	  }
	
	// aes256 keys are 32 chars
	//standard aes256 bit decryption
	public static String aesDecrypt(String dec, String k, String is){
		String es = "";
		try
	    {
	        IvParameterSpec iv = new IvParameterSpec(is.getBytes());
	        SecretKeySpec sk = new SecretKeySpec(k.getBytes(), "AES");
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
	        c.init(Cipher.DECRYPT_MODE, sk, iv);
	        byte[] o = c.doFinal(Base64.decodeBase64(dec));
	        es = new String(o);
	        return es;
	    } 
	    catch (Exception e) {
	    }
		return es;
	}
	
	// standard aes256 bit encryption
	public static String aesEncrypt(String tbe, String k, String is) {
		String s = "";
		try
	    {
	        IvParameterSpec iv = new IvParameterSpec(is.getBytes());
	        SecretKeySpec sk = new SecretKeySpec(k.getBytes(), "AES");
	        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
	        c.init(Cipher.ENCRYPT_MODE, sk, iv);
	        byte[] encr = c.doFinal(tbe.getBytes());
	        s = Base64.encodeBase64String(encr);
	    } 
	    catch (Exception e) 
	    {
	        
	    }
		return s;
	}
	
	public static String keyGen(int si) {
		String str = "-_=+{}[]!@#$%^&*()|?><`~:abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-_=+{}[]!@#$%^&*()|?><:~`";
        StringBuilder strBuild = new StringBuilder();
        Random rand = new Random();
        while (strBuild.length() < si) { // length of the random string.
            int index = (int) (rand.nextFloat() * str.length());
            strBuild.append(str.charAt(index));
        }
        String finalStr = strBuild.toString();
        return finalStr;
	}
	
	// my encryption method :)
	public static String encrypt(String encryptme, String key, String iv) {
		int offset = 276;
		int salt = 736582932;
		int salt1 = 3480;
		int salt2 = 239853045;
	    String encrypted = "";
	    // scan through the inputted string and if they have a space replace it with a nontypable char
	    for(int i = 0; i <= encryptme.length()-1; i++){
	      if(encryptme.charAt(i) == ' '){
	        encrypted = encrypted + (offset + 218);
	        if(i % 4 == 0) {
	        	encrypted = encrypted + salt1;
	        }
	      }else{
	    	  // if the char isnt a space add the offset to it and add it to the string
	         encrypted = encrypted + (Math.pow(offset + (int)encryptme.charAt(i), 3));
	         if(i % 4 == 0) {
		        	encrypted = encrypted + salt2;
		     }
	      }
	    }
	        
	    int equal = offset + 193; // a marker that isnt a typable char
	    int underscore = offset + ((char)'_');// underscores for length
	    String x = encrypted + salt + encrypted + equal + salt2 + encrypted + equal + underscore + underscore + encrypted + salt + salt1 + salt2 + encrypted + offset + salt1 + encrypted;
	    String q = "";
	    char r;
	    String o = "";
	    for(int i = 0; i < x.length(); i++){
	      int u = Integer.valueOf(x.charAt(i));
	      o = o + Integer.toString(u);
	    }
	    
	    for(int i = 0; i < o.length(); i++){
	      
	      String d = o.substring(i, i+1);
	      int e = Integer.valueOf(d);
	      r = (char)(e + 'e');
	      if((i % 4) == 0){
	    	 r = Character.toUpperCase(r);
	         q = q + r + " ";
	      }else{
	         q = q + r;
	      }
	    }
	    q = aesEncrypt(q, key, iv);	
	    return q;
	  }
	
	// this function is for getting keyFile text
	public static String getFileText(String dir) throws FileNotFoundException {
		Scanner sc;
		File f = new File(dir);
		sc = new Scanner(f);
		String str = "";
		while(sc.hasNext()) {
			str += sc.next();
		}
		sc.close();
		return str;
	}
	
	// this function takes a file directory "fileToLoad" and takes all the data and
	// sticks it in an array lisit
	public static void loadFile(String fileToLoad) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(fileToLoad));
		while(scan.hasNext()) {
			alist.add(scan.next());
		}
		scan.close();
	}

	// our global variables
	private static String s = "";
	private static String impS = "";
	private static String fileToLoad = "";
	private static String p;
	private static String u;
	private static ArrayList<String> alist = new ArrayList<String>();
	private static ArrayList<items> itemArr = new ArrayList<items>();
	private static String createFilePath = "";
	private static String title;
	private static int currentElement = -1;
	
	public static void main(String args[]){
		
		// here are our fonts
		Font f1 = new Font("SansSerif ", Font.BOLD, 10);
		Font f2 = new Font("SansSerif ", Font.BOLD, 6);
		Font f3 = new Font("SansSerif ", Font.BOLD, 11);
		
		//jframe stuff
		JButton newBtn = new JButton("Create");
		newBtn.setBounds(35, 30, 75, 25);
		
		JButton loadBtn = new JButton("Load");
		loadBtn.setBounds(125, 30, 75, 25);
		
		JFrame startWindow = new JFrame("Cryptic");
		startWindow.setResizable(false);
		startWindow.setLayout(null);
		startWindow.setVisible(true);
		startWindow.setSize(250, 125);
		startWindow.add(loadBtn);
		startWindow.add(newBtn);
		startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JTextPane loginWindowTitle = new JTextPane();
		loginWindowTitle.setEditable(false);
		loginWindowTitle.setText("Enter your username and password");
		loginWindowTitle.setBounds(150, 50, 200, 25);
		
		JTextPane loginWindowUPanel = new JTextPane();
		loginWindowUPanel.setEditable(true);
		loginWindowUPanel.setText("username");
		loginWindowUPanel.setBounds(150, 100, 125, 25);
		
		JTextPane loginWindowPPanel = new JTextPane();
		loginWindowPPanel.setEditable(true);
		loginWindowPPanel.setText("password");
		loginWindowPPanel.setBounds(150, 130, 125, 25);
		
		JButton loginWindowBackBtn = new JButton("<-");
		loginWindowBackBtn.setFont(f2);
		loginWindowBackBtn.setBounds(5, 5, 40, 20);
		
		JButton loginWindowBtn = new JButton("Login");
		loginWindowBtn.setBounds(280, 100, 70, 55);
		
		JTextPane loginWindowKFPane = new JTextPane();
		loginWindowKFPane.setText("Keyfile address");
		loginWindowKFPane.setEditable(true);
		loginWindowKFPane.setBounds(150, 160, 125, 25);
		
		JButton browseBtn = new JButton("Browse");
		browseBtn.setBounds(280, 160, 70, 25);
		browseBtn.setFont(f1);
		
		JFrame loginWindow = new JFrame("Cipherer");
		
		loginWindow.setVisible(false);
		loginWindow.setSize(500, 250);
		loginWindow.setResizable(false);
		loginWindow.setLayout(null);;
		loginWindow.add(loginWindowTitle);
		loginWindow.add(loginWindowUPanel);
		loginWindow.add(loginWindowPPanel);
		loginWindow.add(loginWindowKFPane);
		loginWindow.add(loginWindowBackBtn);
		loginWindow.add(loginWindowBtn);
		loginWindow.add(browseBtn);
		loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// the login window back button which kicks you to the start screen
		loginWindowBackBtn.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  loginWindow.dispose();
		    	  loginWindowUPanel.setText("username");
		    	  loginWindowPPanel.setText("password");
		    	  loginWindowKFPane.setText("Keyfile address");
		    	  startWindow.setVisible(true);
		      }
		  });
		  
		// on main startWindow when clicked starts the new vault creation process
		newBtn.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	  //on createWindow1
		    	  JTextPane createUsername = new JTextPane();
		    	  createUsername.setText("Enter a username");
		    	  createUsername.setFont(f1);
		    	  createUsername.setBounds(90, 30, 150, 20);
		    	  
		    	  JTextPane createPassword = new JTextPane();
		    	  createPassword.setText("Enter a password");
		    	  createPassword.setFont(f1);
		    	  createPassword.setBounds(90, 55, 150, 20);
		    	  
		    	  JTextPane createConfirmPassword = new JTextPane();
		    	  createConfirmPassword.setText("Reenter your password");
		    	  createConfirmPassword.setFont(f1);
		    	  createConfirmPassword.setBounds(90, 80, 150, 20);
		    	  
		    	  JButton createButton = new JButton("Create");
		    	  createButton.setBounds(255, 30, 95, 70);
		    	  
		    	  // on createWindow
		    	  JButton createBrowserBtn = new JButton("Browse");
		    	  createBrowserBtn.setFont(f1);
		    	  createBrowserBtn.setBounds(84, 65, 70, 25);
		    	  
		    	  JTextPane infoPane = new JTextPane();
		    	  infoPane.setText("Select the folder you wish to build to and enter a deisred name for the file.");
		    	  infoPane.setFont(f3);
		    	  infoPane.setEditable(false);
		    	  infoPane.setBounds(27, 5, 180, 50);
		    	  
		    	  JFrame createWindow = new JFrame("Cryptic");
		    	  createWindow.setVisible(true);
		    	  createWindow.setSize(250, 150);
		    	  createWindow.setResizable(false);
		    	  createWindow.setLayout(null);
		    	  createWindow.add(infoPane);
		    	  createWindow.add(createBrowserBtn);
		    	  createWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    	  
		    	  
		    	  // when clicked opens a browser for the user to find where they 
		    	  // want to create their vault and keyfile at and what to name it
		    	  createBrowserBtn.addActionListener(new ActionListener() {
				      public void actionPerformed(ActionEvent arg0) {
				    	  JFileChooser fileChooser = new JFileChooser();
				    	  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				    	  int result = fileChooser.showOpenDialog(fileChooser);
				    	  if (result == JFileChooser.APPROVE_OPTION) {
				    	      File selectedFile = fileChooser.getSelectedFile();
				    	      createFilePath = selectedFile.getAbsolutePath();
				    	      createWindow.setVisible(false);
				    	      // final window in the create method
				    	      JFrame createWindow1 = new JFrame("Cryptic");
				    	      createWindow1.setVisible(true);
				    	      createWindow1.setResizable(false);
				    	      createWindow1.setLayout(null);
				    	      createWindow1.setSize(450, 200);
				    	      createWindow1.add(createUsername);
				    	      createWindow1.add(createPassword);
				    	      createWindow1.add(createConfirmPassword);
				    	      createWindow1.add(createButton);
				    	      createWindow1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				    	      
				    	      // once they've set a path and a name they need to input
				    	      // a username password and confirm that password
				    	      // Once they've done that it creates 2 files a
				    	      // .kezx file which is the vault and a .kf file which is the keyfile
				    	      // it the creates a random key and encrypts the username and password
				    	      createButton.addActionListener(new ActionListener() {
				    		      public void actionPerformed(ActionEvent arg0) {
				    		    	  // see if the passwords match
				    		    	  if(createPassword.getText().compareTo(createConfirmPassword.getText()) == 0){
				    		    		  // create files
				    		    		  File f = new File(createFilePath + ".kezx");
				    		    		  File fKey = new File(createFilePath + ".kf");
				    		    	      try {
				    		    	    	  // do the keygen
				    		    	    	  String k = keyGen(48);
				    		    	    	  // print the key and the encrypted user pass to files
				    		    	          PrintWriter outp = new PrintWriter(fKey);
				    		    	          outp.println(k);
				    		    	          outp.close();
				    		    	          
				    		    	          PrintWriter out = new PrintWriter(f);
				    		    	          out.println(encrypt(createUsername.getText(), k.substring(0, k.length()-16), k.substring(k.length()-16)));
				    		    	          out.println();
				    		    	          out.println(encrypt(createPassword.getText(), k.substring(0, k.length()-16), k.substring(k.length()-16)));
				    		    	          out.close();
				    		    	          // reset the text inputs
				    		    	          k = "";
				    		    	          createUsername.setText("Enter a username");
				    		    	          createPassword.setText("Enter a password");
				    		    	          createConfirmPassword.setText("Reenter your password");
				    		    	      }catch(IOException ex) {
				    		    	      
				    		    	      }
				    		    	      createWindow1.dispose();
				    		    	      createWindow.dispose();
				    		    	      startWindow.setVisible(true);
				    		    	  }else{
				    		    		  // if the passwords dont match make them red
				    		    		  createPassword.setBackground(Color.red);
				    		    		  createConfirmPassword.setBackground(Color.red);
				    		    	  }
				    		      }
				    		  }); 
				    	  }
				      }
				});
		      }
		});
		
		// on startWindow when pressed prompts a user to set a path for a file to load 
		loadBtn.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  
		    	  JFileChooser fileChooser = new JFileChooser();
		    	  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		    	  int result = fileChooser.showOpenDialog(fileChooser);
		    	  if (result == JFileChooser.APPROVE_OPTION) {
		    	      File selectedFile = fileChooser.getSelectedFile();
		    	      fileToLoad = selectedFile.getAbsolutePath();
		    	      // once the user has set a path it attempts to load the file
		    	      try {
						loadFile(fileToLoad);
						p = alist.get(0);
						u = alist.get(1);
		    	      } catch (Exception e) {
		    	    	  
		    	      }
		    	      loginWindow.setVisible(true);
			    	  startWindow.setVisible(false);
		    	  } 
		      }
		  });	
		
		// on login screen when pressed prompts the user to select a filepath for 
		// their keyfile
		browseBtn.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  JFileChooser fileChooser = new JFileChooser();
		    	  fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		    	  int result = fileChooser.showOpenDialog(fileChooser);
		    	  if (result == JFileChooser.APPROVE_OPTION) {
		    	      File selectedFile = fileChooser.getSelectedFile();
		    	      loginWindowKFPane.setText(selectedFile.getAbsolutePath());
		    	  }
		      }
		  });
		
		// this is the button on the loginWindow that attempts to log you in
		loginWindowBtn.addActionListener(new ActionListener() {
		      public void actionPerformed(ActionEvent arg0) {
		    	  // set s and impS to nothing then pull their text from the keyfile
		    	  // seperate it into the aes key and the iv key
		    	  try {
		    	  s = getFileText(loginWindowKFPane.getText());
		    	  impS = s.substring(s.length()- 16);
		    	  s = s.substring(0, s.length()- 16);
		    	  }catch(Exception e) {
		    		  
		    	  }
		    	  
		    	  // run the username and password through the encryption and see if they match
		    	  if(encrypt(loginWindowUPanel.getText(), s, impS).compareTo(p) == 0 && encrypt(loginWindowPPanel.getText(), s, impS).compareTo(u) == 0){
		    		  
		    		  // if we good put them into the granted window
		    		  loginWindow.setVisible(false);
		    		  
		    		  JTextPane saveTitlePane = new JTextPane();
		    		  saveTitlePane.setBounds(300, 75, 250, 25);
		    		  saveTitlePane.setText("Title");
		    		  
		    		  JTextPane saveUserPane = new JTextPane();
		    		  saveUserPane.setBounds(300, 105, 250, 25);
		    		  saveUserPane.setText("Username");
		    		  
		    		  JTextPane savePassPane = new JTextPane();
		    		  savePassPane.setBounds(300, 135, 250, 25);
		    		  savePassPane.setText("Password");
		    		  
		    		  JTextPane saveNotePane = new JTextPane();
		    		  saveNotePane.setBounds(300, 165, 250, 320);
		    		  saveNotePane.setText("Notes");
		    		  
		    		  JButton deleteBtn = new JButton("Delete");
		    		  deleteBtn.setBounds(345, 5, 80, 25);
		    		  
		    		  JButton saveBtn = new JButton("Save");
		    		  saveBtn.setBounds(430, 5, 70, 25);
		    		  
		    		  JButton newPwdBtn = new JButton("New");
		    		  newPwdBtn.setBounds(280, 5, 60, 25);
		    		  
		    		  JButton lgtout = new JButton("Logout");
		    		  lgtout.setBounds(505, 5, 80, 25);		  
		    		  
		    		  JList<items> itemList = new JList<items>();
		    		  DefaultListModel<items> itemModel = new DefaultListModel<items>();
		    		  itemList.setModel(itemModel);
		    		  
		    		  // the scroll pane
		    		  JScrollPane scroller = new JScrollPane(itemList);
		    		  scroller.setBounds(35, 75, 250, 410);
		    		 
		    		  // the listener on scroller
		    		  ListSelectionListener listSelectionListener = new ListSelectionListener() {
		    		      public void valueChanged(ListSelectionEvent e) {
		    		    	  if(currentElement != -1 && itemArr.size() >= 1) {
		    		    		  itemArr.get(currentElement).setU_DATA(saveUserPane.getText());
		    		          	  itemArr.get(currentElement).setP_DATA(savePassPane.getText());
		    		          	  itemArr.get(currentElement).setN_DATA(saveNotePane.getText());
		    		          	  itemArr.get(currentElement).setT_DATA(saveTitlePane.getText());	
		    		          	  currentElement = itemList.getSelectedIndex();
		    		          	  if(currentElement != -1) {
		    		          	  saveUserPane.setText(itemArr.get(currentElement).getU_DATA());
		    		          	  savePassPane.setText(itemArr.get(currentElement).getP_DATA());
		    		          	  saveNotePane.setText(itemArr.get(currentElement).getN_DATA());
		    		          	  saveTitlePane.setText(itemArr.get(currentElement).getT_DATA());
		    		          	  }
		    		          }else if(itemArr.size() >= 1 && currentElement == -1){
		    		   			  saveUserPane.setText(itemArr.get(0).getU_DATA());
		    		   			  savePassPane.setText(itemArr.get(0).getP_DATA());
		    		   			  saveNotePane.setText(itemArr.get(0).getN_DATA());
		    		   			  saveTitlePane.setText(itemArr.get(0).getT_DATA());
		    		    		  currentElement = 0;
		    		    	  }
		    		      }
		    		    };
		    		  itemList.addListSelectionListener(listSelectionListener);
		    		  
		    		  JFrame granted = new JFrame("Cryptic: Welcome " + loginWindowUPanel.getText());
		    		  granted.setVisible(true);
		    		  granted.setSize(600, 575);
		    		  granted.setResizable(false);
		    		  granted.setLayout(null);
		    		  granted.add(lgtout);
		    		  granted.add(saveUserPane);
		    		  granted.add(savePassPane);
		    		  granted.add(saveNotePane);
		    		  granted.add(saveTitlePane);
		    		  granted.add(saveBtn);
		    		  granted.add(newPwdBtn);
		    		  granted.add(scroller);
		    		  granted.add(deleteBtn);
		    		  title = granted.getTitle();
		    		  granted.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    		  
		    		  // the window that confirms you wanna delete something
		    		  JButton imSureButton = new JButton("Yes im Sure");
		    		  imSureButton.setBounds(40, 30, 150, 50);
		    		  
		    		  JFrame areYouSure = new JFrame("You sure?");
		    		  areYouSure.setSize(250, 150);
		    		  areYouSure.setResizable(false);
		    		  areYouSure.setVisible(false);
		    		  areYouSure.setLayout(null);
		    		  areYouSure.add(imSureButton);
		    		  areYouSure.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    		  
		    		  // decrypt the items in the file and display them
		    		  int[] darray;
		    		  for(int i = 2; i < alist.size(); i += 4) {
		    			  itemArr.add(new items(ourDecrypt(alist.get(i), darray = new int[alist.get(i).length()], s, impS), ourDecrypt(alist.get(i+1), darray = new int[alist.get(i+1).length()], s, impS), ourDecrypt(alist.get(i+2), darray = new int[alist.get(i+2).length()], s, impS), ourDecrypt(alist.get(i+3), darray = new int[alist.get(i+3).length()], s, impS)));
		    			  itemModel.addElement(itemArr.get(itemArr.size() - 1));
		    		  }
		    		  
		    		  //reset the input panels
		    		  loginWindowUPanel.setText("username");
		    		  loginWindowPPanel.setText("password");
		    		  loginWindowKFPane.setText("Keyfile address");
		    		  
		    		  deleteBtn.addActionListener(new ActionListener() {
		    		      public void actionPerformed(ActionEvent arg0) {
		    		    	  areYouSure.setVisible(true);
		    		      }
		    		  });
		    		  
		    		  // the confirmation button to delete it hides the confirm button
		    		  // and them removes the element from the arrayList then it resets the jlist
		    		  imSureButton.addActionListener(new ActionListener() {
		    		      public void actionPerformed(ActionEvent arg0) {
		    		    	  if(currentElement != -1) {
		    		    		  areYouSure.dispose();
		    		    		  if(currentElement != 0) {
		    		    			  itemArr.remove(currentElement);
		    		    			  currentElement = -1;
		    		    			  itemModel.removeAllElements();
		    		    			  itemModel.addAll(itemArr);
		    		    		  }else if(currentElement == 0 && itemModel.getSize() == 1) {
		    		    			  itemArr.clear();
		    		    			  itemModel.clear();
		    		    			  currentElement = -1;
		    		    		  }else if(currentElement == 0) {
		    		    			  itemArr.remove(0);
		    		    			  currentElement = -1;
		    		    			  itemModel.removeAllElements();
		    		    			  itemModel.addAll(itemArr);
		    		    		  }
	    		    			  granted.setTitle("*" + title);

		    		    	  }
		    		      }
		    		  });
		    		  
		    		  // create a new items object and add it to itemArr nad itemModel
		    		  newPwdBtn.addActionListener(new ActionListener() {
		    		      public void actionPerformed(ActionEvent arg0) {
		    		    	  itemArr.add(new items("Username", "Password", "Notes", "Title"));
		    		    	  itemModel.addElement(itemArr.get(itemArr.size() - 1));
		    		    	  granted.setTitle("*" + title);
		    		      }
		    		  });
		    		  
		    		  // the save button
		    		  saveBtn.addActionListener(new ActionListener() {
		    		      public void actionPerformed(ActionEvent arg0) {
		    		    	  granted.setTitle(title);
		    		    	  
		    		    	  
		    		    	  try {
		    		    		  // clear the file
		    		    		  PrintWriter writer = new PrintWriter(fileToLoad);
		    		    		  writer.print("");
		    		    		  writer.close();
			    		    	
		    		    		  // generate salty bois
		    		    		  int salt = (int)(Math.random() * ((745463293 - 234652384) + 1)) + 234652384;
		    		    		  int salt1 = (int)(Math.random() * ((9623 - 1429) + 1)) + 1429;
		    		    		  int salt2 = (int)(Math.random() * ((745463293 - 234652384) + 1)) + 234652384;
		    		    		  int offset = (int)(Math.random() * ((391 - 100) + 1)) + 100;

		    		    		  // write to file
		    		    		  PrintWriter out = new PrintWriter(fileToLoad);
		    		    		  out.println(alist.get(0));
		    		    		  out.println();
		    		    		  out.println(alist.get(1));
		    		    		  out.println();
		    		    		  if(itemArr.size() != 0) {
		    		    			  for(int i = 0; i < itemArr.size(); i++) {
		    		    				  out.println(ourEncrypt(itemArr.get(i).getU_DATA(),offset, salt, salt1, salt2, s, impS));
		    		    				  out.println();
		    		    				  out.println(ourEncrypt(itemArr.get(i).getP_DATA(),offset, salt, salt1, salt2, s, impS));
		    		    				  out.println();
		    		    				  out.println(ourEncrypt(itemArr.get(i).getN_DATA(),offset, salt, salt1, salt2, s, impS));
		    		    				  out.println();
		    		    				  out.println(ourEncrypt(itemArr.get(i).getT_DATA(),offset, salt, salt1, salt2, s, impS));
		    		    				  out.println();
		    		    			  }
		    		    		  }
		    		    		  out.close();
		    		    	  } catch (FileNotFoundException e) {

		    		    	  }
		    		      }
		    		  });
		    		  
		    		  // if they press logout it closes all windows and kicks them back to the startWindow
		    		  lgtout.addActionListener(new ActionListener() {
		    		      public void actionPerformed(ActionEvent arg0) {
		    		    	  granted.dispose();
		    		    	  loginWindow.dispose();
		    		    	  alist.clear();
		    		    	  itemArr.clear();
		    		    	  currentElement = -1;
		    		    	  fileToLoad = "";
		    		    	  p = "";
		    		    	  u = "";
		    		    	  s = "";
		    		    	  impS = "";
		    		    	  startWindow.setVisible(true);
		    		      }
		    		  }); 
		    	  }
		      }
		});
	}
}