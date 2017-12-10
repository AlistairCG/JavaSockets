package a3;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
/*
 * Alistair Godwin
/*
 * 
 * This program designs a java typing tutor which accepts key events from a user.
 * The program will highlight valid keys upon entry by the user. 
 * The keyboard features a toggle based keyboard which will change case.
 * 
 *
 */
@SuppressWarnings("serial")
public class Keyboard extends JFrame implements KeyListener{
	//=======Components and Containers==============//
	private JPanel Contain = new JPanel();
	private JPanel Padding = new JPanel();
	private JPanel Lbl  = new JPanel();
	private JPanel Keys = new JPanel();
	private JTextArea TextArea = new JTextArea("Tap shift or caps for lowercase keys! ");
	private JScrollPane TxtArea_ = new JScrollPane(TextArea);
	private JPanel Hold = new JPanel();
	protected static JPanel Kb = new JPanel();
	private JLabel Lbl_txt = new JLabel();
	
	public KeyReciever keyrecv = new KeyReciever(); //<-- The Server
	
	//===============================//
	//=============Keyboard Arrays============//
	private static final String [] KeysText = {"~","1","2","3","4","5","6","7","8","9","0","-","+","Backspace",
											"Tab","Q","W","E","R","T","Y","U","I","O","P","[","]","\\", //27
											"Caps","A","S","D","F","G","H","J","K","L",":","\"","Enter", //40
											"Shift","Z","X","C","V","B","N","M",",",".","?","","^", " "," "," ","<"," v ",">"};
	protected static final String [] Keystext_L = {"~","1","2","3","4","5","6","7","8","9","0","-","+","Backspace",
												"Tab","q","w","e","r","t","y","u","i","o","p","[","]","\\", //27
												"Caps","a","s","d","f","g","h","j","k","l",":","\"","Enter", //40
												"Shift","z","x","c","v","b","n","m",",",".","?","","^", " "," "," ","<"," v ",">"};
	//==========================================//
	//===========Flags=========//
	private boolean isCaps = true; //Flag since I cant check capslock on reasonably
	private boolean isSpam = false; //So shift, backspace, etc cant be held down and destroy my syso.
	//=========================//
	
	
	//Contact!
	public Keyboard() {
		
		this.setTitle("Typing Tutor");
		this.setLayout(new FlowLayout(FlowLayout.LEFT,10,5));		
		this.setSize(858, 630);  
		
		//Global Container 
		Contain.setLayout(new BoxLayout(Contain,BoxLayout.Y_AXIS));
		//Label Container
		Lbl.setLayout(new FlowLayout(FlowLayout.LEADING));
		//Holds Both the Lbl and Text area as required by assignment dimensions
		Hold.setLayout(new BoxLayout(Hold,BoxLayout.Y_AXIS));	//Make Hold place components
		//Eat 100% of available panel space
		Keys.setLayout(new GridLayout(1,1));
		
		//Create layout and invoke key creation
		Kb.setLayout(new FlowLayout(FlowLayout.LEADING,0,0));
		SetKeys(Kb);
		
		//==============TextArea=============//
		//Prevents shaking component and makes sure text wraps.
		//Wrapping via Javadocs style
		TextArea.addKeyListener(this);
		TextArea.setLineWrap(true);
		TextArea.setWrapStyleWord(true);
		//=============================//
	
		//Display text, note HTML since JLabel supports it and means less Panels.
		Lbl_txt.setText("<html><body><p>Type some text using your keyboard. The keys you press will be highlighted and the text"
				+ " will be displayed.</p><p>Note: Clicking the buttons with your mouse will not perform any action </p></body> </html>");

		//Make space as specified by the assignment.
		Lbl.setPreferredSize(new Dimension(827,40));
		Keys.setPreferredSize(new Dimension(420,210));
		Kb.setPreferredSize(new Dimension(827,300));
	
		//Make an invisible divider while keeping the components vertically stacked
		Padding.setPreferredSize(new Dimension(25,20));
			
		Kb.setFocusTraversalKeysEnabled(true);
		//=======Add all components to the Frame and Panel(s)====//
		Lbl.add(Lbl_txt);
		Keys.add(TxtArea_);
		Hold.add(Lbl);
		Hold.add(Keys);
		Contain.add(Hold);
		Contain.add(Padding);
		Contain.add(Kb);
		add(Contain); 		
		//============================================//
		
		//Set behavior
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.setResizable(true);
		
		
		
	}

	public void keyTyped(KeyEvent type){
		
		//----//
		
	}
	
	//Actions undertaken on key released. Mostly reset flags and uncaps shift
	public void keyReleased(KeyEvent type) {
		//Get Standard Key info
		int insp = type.getKeyCode();
		String Key = KeyEvent.getKeyText(insp);
		Key = ReplacewithString(Key);
		
		
		//Remove highlight
		HighLightKey(Key, false);
		
		if(insp == KeyEvent.VK_SHIFT)	
		SwitchCase(true); 		//Switch!
		  isSpam = false;     //Reset spam prevention flag
		  
		  
	}
	
	//Inspect the key and prevent overall spam from backspace and shift.
	//Allow Shift + chars.
	public void keyPressed(KeyEvent type) {
	
      //Fetch KeyEvent information
		int insp = type.getKeyCode();
		String Key = KeyEvent.getKeyText(insp);
	
	//Check for spam and declare valid state	
		boolean Spam = (insp == KeyEvent.VK_SHIFT || insp == KeyEvent.VK_BACK_SPACE) ? true : false; 
		boolean isValid;
		

		//Useful, reduces strain by only performing 1 array check vs multiple conditional checks.
		if(isSpam == false) {
			//Inspect the Key and as required change the key text to match keyboard
			Key = ReplacewithString(Key);	
			//Check if Key matches the available keyboard.
			isValid = isMemberOfArray(Key);
			
		}else {
		
			//Shift + letter is a-ok, just not shift on its own.
			if(Key.matches("[a-zA-Z]") == true){
				isValid = true; //Well a-z is part of keyboard...
				Spam = false;
				isSpam = false;
				
				
			}else {
				//Reset flags and change some string to match the keyboard.
				switch(Key) {
					case "Slash":
					case "Back Quote":
					case "Quote":
					case "Semicolon":
					case "Equals": //Actually '+'
					Key = ReplacewithString(Key);
					isValid = isMemberOfArray(Key);
					//Reset flags
					Spam = false;
					isSpam = false;
					break;
				default:
					isValid = false;
				
				}
			}
		
		}
		
		
		//============Check for key spam and valid key entry========//
		  if(isValid == true && (Spam == false)){
			 HighLightKey(Key, true);
			 if(insp == KeyEvent.VK_CAPS_LOCK) {
				 	SwitchCase(!isCaps);
				 
			 }
			 
		  }//Usable for shift only when done in succession to another shift or backspace.
		  else if(isValid == true && (Spam == true) && (isSpam == false)) {
			  isSpam = true; // Prevent the spam of useless chars to syso by flag
			  HighLightKey(Key, true); //Light up the night
			  if(insp == KeyEvent.VK_SHIFT)	//Change keyboard case on switch.
					SwitchCase(false);			  
		  }else if( isValid == true && isSpam == true) {
			 //..Do nothing..//
		  }
		  
		  //==================================================//
		  
	}
		//Server Client Receives data, highlight relevant key
		//Allow Shift + chars.
		public void keyPressed_Client(int insp) {
			
			String Key = KeyEvent.getKeyText(insp);
			Key = ReplacewithString(Key);
			
			//Check for spam and declare valid state	
			boolean Spam = (insp == KeyEvent.VK_SHIFT || insp == KeyEvent.VK_BACK_SPACE) ? true : false; 
			boolean isValid;
			

			//Useful, reduces strain by only performing 1 array check vs multiple conditional checks.
			if(isSpam == false) {
				//Inspect the Key and as required change the key text to match keyboard
				Key = ReplacewithString(Key);	
				//Check if Key matches the available keyboard.
				isValid = isMemberOfArray(Key);
				
			}else {
			
				//Shift + letter is a-ok, just not shift on its own.
				if(Key.matches("[a-zA-Z]") == true){
					isValid = true; //Well a-z is part of keyboard...
					Spam = false;
					isSpam = false;
			
					
				}else {
					//Reset flags and change some string to match the keyboard.
					switch(Key) {
						case "Slash":
						case "Back Quote":
						case "Quote":
						case "Semicolon":
						case "Equals": //Actually '+'
						Key = ReplacewithString(Key);
						isValid = isMemberOfArray(Key);
						//Reset flags
						Spam = false;
						isSpam = false;
						break;
					default:
						isValid = false;
					
					}
				}
			
			}
			
			
			//============Check for key spam and valid key entry========//
			  if(isValid == true && (Spam == false)){
				 HighLightKey(Key, true);
				 if(insp == KeyEvent.VK_CAPS_LOCK) {
					 	SwitchCase(!isCaps);
					 
				 }
				 
			  }//Usable for shift only when done in succession to another shift or backspace.
			  else if(isValid == true && (Spam == true) && (isSpam == false)) {
				  isSpam = true; // Prevent the spam of useless chars to syso by flag
				  HighLightKey(Key, true); //Light up the night
				  if(insp == KeyEvent.VK_SHIFT)	//Change keyboard case on switch.
						SwitchCase(false);			  
			  }else if( isValid == true && isSpam == true) {
				 //..Do nothing..//
			  }
			  
			  //==================================================//
			  
		}
		
		
		
	//Highlight a key based on if the input string being found in one of the components of Kb.
	//Will turn off the highlight when boolean is false.
	private void HighLightKey(String Key, boolean light) {
		//==========Highlight=======//

		if(light == true) {
			System.out.println("This key pressed -->" + Key + "<--");
			for(Component c : Kb.getComponents()) //Bless Stackoverflow {
					
				if(c instanceof JButton) {	
					if(((JButton) c).getText().equalsIgnoreCase(Key)) {
						((JButton) c).setBackground((new Color(25,125,125)));
						break;
					}		
				}
				
					
			}
		//==================//
		//==========Remove Highlight=========//
		else {
			for(Component c : Kb.getComponents()) {
				if(c instanceof JButton) { //Bless you stackoverflow
					
					if(((JButton) c).getText().equalsIgnoreCase(Key)) {
						((JButton) c).setBackground(new JButton().getBackground()); //Default
						break;
					}	
					
					
				}
			}
				
		}
		//========================//
			
		
	}
	//Will make caps when caps lock or shift is pressed(or depressed :( )
	private void SwitchCase(boolean Capital) {
		int i = 0; //Counter
		isCaps = Capital;
		//Iterate through the keyboard
		for(Component c : Kb.getComponents()) { //Fancy

			if(c instanceof JButton) { //Bless you stackoverflow
				if(Capital == true) {
					//Change some text to the othercase equivalent
					((JButton) c).setText(Keystext_L[i]);
				
						
				}else {
					isCaps = false;
					((JButton) c).setText(KeysText[i]);
				}
			}
				
			i++; //Iterate and set the buttons again.
		
		}
		
	}
		
//Replace the strings as required with the keyboard character equivalent		
	private String ReplacewithString(String key2) {
		switch (key2) {
		case "Slash":
			return "?";
		case "Back Slash":
			return "\\";
		case "Back Quote":
			return "~";
		case "Quote":
			return "\"";
		case "Semicolon":
			return ":";
		case "Open Bracket":
			return "[";
		case "Close Bracket":
			return "]";
		case "Minus":
			return "-";
		case "Equals":
			return "+";
		case "Caps Lock":
			return "Caps";
		case "Comma":
			return ",";
		case "Period":
			return ".";
		case "Up":
			return "^";
		case "Left":
			return "<";
		case "Right":
			return ">";
		case "Space":
			return " ";
		case "Down":
			return " v "; //Easiest hack this side of Alabama. 
		default:
			return key2;
		}
	}
	
	//Check if a string is a member of the array. 
	//Small catch for down arrow
	public boolean isMemberOfArray(String test) {
		 for (String s: KeysText) { //Besides case, KeysText_L isnt any different.
		        if (s.equals(test)) {
		            return true;
		        }
		}
		 return false;
	}
	
	
	//Write some buttons using the array and size them based on the element requirement
	public static void SetKeys(Container kb) {
		for(int i = 0; i < KeysText.length; i++) {
		
			switch(i) {
			//Make a Double Sized key
				case 41:
				case 40:
				case 13: 
					JButton button_Ext = new JButton(GetNextKey(i));
					button_Ext.setAlignmentX(Component.CENTER_ALIGNMENT);
					button_Ext.setPreferredSize(new Dimension(110,60));
					button_Ext.setFont(new Font("Arial",Font.BOLD,12));

					kb.add(button_Ext);
					break;
		
			//Make a 1.5 Sized Key
				case 28:
				case 14: 
					JButton button_FullHalf = new JButton(GetNextKey(i));
					button_FullHalf.setAlignmentX(Component.CENTER_ALIGNMENT);
					button_FullHalf.setPreferredSize(new Dimension(86,60));
					button_FullHalf.setFont(new Font("Arial",Font.BOLD,12));
					kb.add(button_FullHalf);
					break;
			
			//Make a .5 sized invisible panel
				case 52:
					JPanel button_Half = new JPanel();
					button_Half.setPreferredSize(new Dimension(31,60));
					button_Half.setVisible(true);
					kb.add(button_Half);
					break;
			
			//Make a space divider for the space bar
				case 54:
					JPanel divide = new JPanel();
					divide.setPreferredSize(new Dimension(218,60));
					divide.setEnabled(true);
					divide.setVisible(true);
					kb.add(divide);
					break;
			//Make a space bar key	
				case 55:
					JButton button_Space = new JButton(GetNextKey(i));
					button_Space.setPreferredSize(new Dimension(335,60));
					kb.add(button_Space);
					break;
			//Make a r-side divider for the arrow keys
				case 56:
					JPanel divide_r = new JPanel();
					divide_r.setPreferredSize(new Dimension(83,60));
					divide_r.setEnabled(true);
					divide_r.setVisible(true);
					kb.add(divide_r);
					break;
			//Make a standard key
				default:
					JButton button = new JButton(GetNextKey(i));
					button.setAlignmentX(Component.CENTER_ALIGNMENT);
					button.setPreferredSize(new Dimension(55,60));
					button.setFont(new Font("Arial",Font.BOLD,12));
					kb.add(button);
					break;
			}
	
		}
		
		
	}
	
	//Return KeysText[element]
	public static String GetNextKey(int arrAt) {
		return KeysText[arrAt];
	}
	
	//Contact!
	public static void main (String [] args) {
		new Keyboard();
	}

}