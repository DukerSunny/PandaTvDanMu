package com.neucrack.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import com.melloware.jintellitype.HotkeyListener;
import com.melloware.jintellitype.JIntellitype;
import com.neucrack.DataPersistence.PreferenceData;
import com.neucrack.Help_Update.Help;
import com.neucrack.protocol.Bamboo;
import com.neucrack.protocol.ConnectDanMuServer;
import com.neucrack.protocol.Danmu;
import com.neucrack.protocol.Gift;
import com.neucrack.protocol.Platform;
import com.neucrack.protocol.User;
import com.neucrack.protocol.Visitors;
import com.sun.awt.AWTUtilities;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.ListModel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ListUI;

import java.awt.Toolkit;

import javax.swing.ImageIcon;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;
import java.util.prefs.Preferences;
import java.awt.FlowLayout;

import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class PandaTVDanmu extends JFrame {

	private JPanel contentPane;
	static Point origin = new Point();
	private JTextField mRoomID;
	private JList<ListItemDanMu> mMessageList;
	final JFrame parentPanel=this;
	private boolean mLock=false;
	private boolean mIsConnectionAlive=false;
	private boolean mIsAutoScroll=true;
	private DefaultListModel<ListItemDanMu> mListItem;
	private int mTransparentValue=0;
	//	DefaultListModel listModel;
	int mMessagelastIndex=0;
	private ConnectDanMuServer mDanMuConnection;
	
	static PandaTVDanmu frame;
	private JLabel mVisitorNum;
	private JPanel panel_header_1;
	private JPanel panel_header_2_left;
	private JLabel label;
	private JLabel mLockHint;
	private JLabel mCloseWindow;
	private JPanel panel_1_left;
	private JPanel panel_2_right;
	private JLabel mStartStopConnection;
	private JLabel mPauseAutoScroll;
	private JScrollPane scrollPane;
	private JLabel mHelp;
	
	//定义热键标识，用于在设置多个热键时，在事件处理中区分用户按下的热键 
	public static final int FUNC_KEY_MARK = 1;
	private JPanel panel_header_2;
	private JPanel panel_header_2_right;
	private JLabel mSettings;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
					UIManager.setLookAndFeel(lookAndFeel);
					frame = new PandaTVDanmu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PandaTVDanmu() {
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_CONTROL){
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				}

			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_CONTROL){
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				}
			}
		});
		setIconImage(Toolkit.getDefaultToolkit().getImage(PandaTVDanmu.class.getResource("/pic/icon.png")));
		setBackground(Color.BLACK);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(1100, 250, 272, 323);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panelHeader = new JPanel();
		panelHeader.setBackground(Color.DARK_GRAY);
		contentPane.add(panelHeader, BorderLayout.NORTH);
		panelHeader.setOpaque(false);
		panelHeader.setLayout(new BorderLayout(0, 0));
		
		panel_header_1 = new JPanel();
		panelHeader.add(panel_header_1, BorderLayout.NORTH);
		panel_header_1.setOpaque(false);
		panel_header_1.setLayout(new BorderLayout(0, 0));
		
		panel_1_left = new JPanel();
		panel_header_1.add(panel_1_left, BorderLayout.WEST);
		panel_1_left.setOpaque(false);
		
		mVisitorNum = new JLabel("0");
		panel_1_left.add(mVisitorNum);
		mVisitorNum.setHorizontalAlignment(SwingConstants.LEFT);
		mVisitorNum.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/audience.png")));
		mVisitorNum.setBackground(Color.DARK_GRAY);
		mVisitorNum.setForeground(Color.WHITE);
		
		panel_2_right = new JPanel();
		panel_header_1.add(panel_2_right, BorderLayout.EAST);
		panel_2_right.setOpaque(false);
		
		mLockHint = new JLabel("F10锁定");
		panel_2_right.add(mLockHint);
		mLockHint.setForeground(Color.WHITE);
		
		mHelp = new JLabel("");
		mHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JDialog helpDialog = new JDialog(parentPanel, "Neucrack_PandaTV 弹幕助手  帮助", null);
				helpDialog.setBounds(500, 150, 550, 323);
				Help helpInfo=new Help();
				helpDialog.getContentPane().setLayout(new FlowLayout());
				JTextArea helpMessage = new JTextArea(helpInfo.getmHelpMessage());
				JTextArea aboutMaker = new JTextArea(helpInfo.getmAboutMaker());
				helpMessage.setOpaque(false);
				aboutMaker.setOpaque(false);
				helpDialog.getContentPane().add(helpMessage);
				helpDialog.getContentPane().add(aboutMaker);
				helpDialog.setVisible(true);
			}
		});
		
		mSettings = new JLabel("");
		mSettings.setCursor(new Cursor(Cursor.HAND_CURSOR));
		mSettings.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JDialog settingsDialog = new JDialog(parentPanel, "Neucrack_PandaTV 弹幕助手  设置", null);
				settingsDialog.setBounds(500, 150, 350, 250);
				settingsDialog.getContentPane().setLayout(new GridLayout(0, 1));
				JPanel transparentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				JPanel rememberRoomIDPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				JPanel applyPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				final JLabel  transparentLabel=new JLabel("透明度 0%");
				final JSlider slider = new JSlider(0, PreferenceData.MAXTRASPARENTVALUE, PreferenceData.MAXTRASPARENTVALUE);
				final JCheckBox isRemenberRoomID = new JCheckBox("保存房间号");
				JButton apply = new JButton("应用");
				slider.setCursor(new Cursor(Cursor.HAND_CURSOR));
				PreferenceData prefData=new PreferenceData();
				mTransparentValue = prefData.getmTransparentValue();
				if(prefData.IsSaveRoomID())
					isRemenberRoomID.setSelected(true);
				else
					isRemenberRoomID.setSelected(false);
				transparentLabel.setText("透明度  "+(PreferenceData.MAXTRASPARENTVALUE-mTransparentValue)+"% ");
				slider.setValue(PreferenceData.MAXTRASPARENTVALUE-mTransparentValue);
				
				transparentPanel.add(transparentLabel);
				transparentPanel.add(slider);
				rememberRoomIDPanel.add(isRemenberRoomID);
				applyPanel.add(apply);
				
				settingsDialog.getContentPane().add(transparentPanel);
				settingsDialog.getContentPane().add(rememberRoomIDPanel);
				settingsDialog.getContentPane().add(applyPanel);
						
				settingsDialog.setVisible(true);
				
				
				slider.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						mTransparentValue = PreferenceData.MAXTRASPARENTVALUE - slider.getValue();
						transparentLabel.setText("透明度  "+(PreferenceData.MAXTRASPARENTVALUE-mTransparentValue)+"% ");
					}
				});
				apply.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						parentPanel.setOpacity((float) ((mTransparentValue+100-PreferenceData.MAXTRASPARENTVALUE)/100.0));
						PreferenceData prefData=new PreferenceData();
						prefData.SaveTransparentValue(mTransparentValue);
						if(isRemenberRoomID.isSelected()){
							prefData.SaveIsSaveRoomID(true);
							prefData.SaveRoomID(mRoomID.getText());
						}
						else
							prefData.SaveIsSaveRoomID(false);
					}
				}); 
			}
		});
		mSettings.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/settings.png")));
		panel_2_right.add(mSettings);
		panel_2_right.add(mHelp);
		mHelp.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/help.png")));
		mHelp.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		mCloseWindow = new JLabel("");
		panel_2_right.add(mCloseWindow);
		mCloseWindow.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/close.png")));
		mCloseWindow.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		panel_header_2 = new JPanel();
		panel_header_2.setOpaque(false);
		panelHeader.add(panel_header_2, BorderLayout.SOUTH);
		panel_header_2.setLayout(new BorderLayout(0, 0));
		
		panel_header_2_left = new JPanel();
		panel_header_2.add(panel_header_2_left, BorderLayout.WEST);
		panel_header_2_left.setOpaque(false);
		GridBagLayout gbl_panel_header_2_left = new GridBagLayout();
		gbl_panel_header_2_left.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_header_2_left.rowHeights = new int[]{0, 0};
		gbl_panel_header_2_left.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_header_2_left.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_header_2_left.setLayout(gbl_panel_header_2_left);
		
		label = new JLabel("房间");
		label.setForeground(Color.WHITE);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		panel_header_2_left.add(label, gbc_label);
		
		mRoomID = new JTextField();
		mRoomID.setBorder(new EmptyBorder(0,0,0,0));
		//加载持久化数据
		PreferenceData prefData=new PreferenceData();
		if(prefData.IsSaveRoomID()){
			mRoomID.setText(prefData.GetRoomID());
		}
		else{
			mRoomID.setText(PreferenceData.DEFAULT_ROOMID);
		}
		GridBagConstraints gbc_mRoomID = new GridBagConstraints();
		gbc_mRoomID.insets = new Insets(0, 0, 0, 5);
		gbc_mRoomID.gridx = 1;
		gbc_mRoomID.gridy = 0;
		panel_header_2_left.add(mRoomID, gbc_mRoomID);
		mRoomID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(mIsConnectionAlive){
						CloseConnection();
						mStartStopConnection.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StartConnection.png")));
						mPauseAutoScroll.setVisible(false);
					}
					StartConnection();
					mStartStopConnection.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StopConnection.png")));
					mPauseAutoScroll.setVisible(true);
					mPauseAutoScroll.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StopAutoscroll.png")));
					PreferenceData prefData = new PreferenceData();
					if(prefData.IsSaveRoomID())
						prefData.SaveRoomID(mRoomID.getText());
					else
						prefData.SaveRoomID(PreferenceData.DEFAULT_ROOMID);
				}
			}
		});
		mRoomID.setBackground(Color.DARK_GRAY);
		mRoomID.setForeground(Color.WHITE);
		mRoomID.setColumns(10);
		
		
		mStartStopConnection = new JLabel("");
		mStartStopConnection.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StartConnection.png")));
		mStartStopConnection.setCursor(new Cursor(Cursor.HAND_CURSOR));
		mStartStopConnection.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(mIsConnectionAlive){
					CloseConnection();
					mStartStopConnection.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StartConnection.png")));
					mPauseAutoScroll.setVisible(false);
				}
				else{
					StartConnection();
					mStartStopConnection.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StopConnection.png")));
					mPauseAutoScroll.setVisible(true);
					mPauseAutoScroll.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StopAutoscroll.png")));
				}
			}
		});
		GridBagConstraints gbc_mStartStopConnection = new GridBagConstraints();
		gbc_mStartStopConnection.insets = new Insets(0, 0, 0, 5);
		gbc_mStartStopConnection.gridx = 2;
		gbc_mStartStopConnection.gridy = 0;
		panel_header_2_left.add(mStartStopConnection, gbc_mStartStopConnection);
		
		mPauseAutoScroll = new JLabel("");
		mPauseAutoScroll.setCursor(new Cursor(Cursor.HAND_CURSOR));
		mPauseAutoScroll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(mIsAutoScroll){//目前是自动滚屏
					mIsAutoScroll=false;
					mPauseAutoScroll.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StartConnection.png")));
				}
				else{
					mIsAutoScroll=true;
					mPauseAutoScroll.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/StopAutoscroll.png")));
				}
			}
		});
		GridBagConstraints gbc_mPauseAutoScroll = new GridBagConstraints();
		gbc_mPauseAutoScroll.insets = new Insets(0, 0, 0, 5);
		gbc_mPauseAutoScroll.gridx = 3;
		gbc_mPauseAutoScroll.gridy = 0;
		panel_header_2_left.add(mPauseAutoScroll, gbc_mPauseAutoScroll);
		
		panel_header_2_right = new JPanel();
		panel_header_2_right.setOpaque(false);
		panel_header_2.add(panel_header_2_right, BorderLayout.EAST);
		mCloseWindow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				mCloseWindow.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/close_hover.png")));
			}
			@Override
			public void mousePressed(MouseEvent e) {
				mCloseWindow.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/close_pressed.png")));
			}
			@Override
			public void mouseExited(MouseEvent e) {
				mCloseWindow.setIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/close.png")));
			}
		});
		
		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0,0));
		scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(0,0));
		scrollPane.setOpaque(false);//设置透明
		scrollPane.getViewport().setOpaque(false);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		mListItem = new DefaultListModel<ListItemDanMu>();
		mMessageList = new JList<ListItemDanMu>(mListItem);
		mMessageList.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_CONTROL){
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_CONTROL){
					scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
					scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
				}
			}
		});
		mMessageList.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// 当鼠标按下的时候获得窗口当前的位置
				if(!mLock){
					origin.x = e.getX();
					origin.y = e.getY();
				}
			}
		});
		mMessageList.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				// 当鼠标拖动时获取窗口当前位置
				if(!mLock){
					Point p =parentPanel.getLocation();
					// 设置窗口的位置
					// 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
					parentPanel.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
				}
			}
		});
		mMessageList.setCellRenderer(new ListRenderer());
		mMessageList.setBorder(null);
		mMessageList.setBackground(new Color(0, 0, 0, 0));
		mMessageList.setOpaque(false);//设置透明
		mMessageList.setBorder(new EmptyBorder(0,0,0,0));
		
		//((JLabel)mMessageList.getCellRenderer()).setOpaque(false);//设置jlist条目透明，不是自己构造listrenderer时使用
		scrollPane.setViewportView(mMessageList);
		
		
		this.setAlwaysOnTop(true);//窗口置顶
		this.setTitle("PandaTVDanMu");
		this.setUndecorated(true);
		//AWTUtilities.setWindowOpacity(this, 1f);//设置透明度
		mTransparentValue = prefData.getmTransparentValue();
		this.setOpacity((float) ((mTransparentValue+100-PreferenceData.MAXTRASPARENTVALUE)/100.0));
		this.validate();
		
		
		
		this.addMouseListener(new MouseAdapter() {
			// 按下（mousePressed 不是点击，而是鼠标被按下没有抬起）
			public void mousePressed(MouseEvent e) {
				// 当鼠标按下的时候获得窗口当前的位置
				if(!mLock){
					origin.x = e.getX();
					origin.y = e.getY();
				}
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter() {
			// 拖动（mouseDragged 指的不是鼠标在窗口中移动，而是用鼠标拖动）
			public void mouseDragged(MouseEvent e) {
				// 当鼠标拖动时获取窗口当前位置
				if(!mLock){
					Point p =parentPanel.getLocation();
					// 设置窗口的位置
					// 窗口当前的位置 + 鼠标当前在窗口的位置 - 鼠标按下的时候在窗口的位置
					parentPanel.setLocation(p.x + e.getX() - origin.x, p.y + e.getY()- origin.y);
				}
			}
		});
		
		
		
		//全局热键
		JIntellitype.getInstance().registerHotKey(FUNC_KEY_MARK,0, KeyEvent.VK_F10);
		JIntellitype.getInstance().addHotKeyListener(new HotkeyListener() {
			
			@Override
			public void onHotKey(int arg0) {
				switch (arg0) { 
				case FUNC_KEY_MARK:	
					if(mLock){
						UnLock();
						mLock = false;
					}
					else{
						Lock();
						mLock=true;
					}
					break;
				}
			}
		});
	}
	private void StartConnection(){
		UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "连接中。。。", null, null, null));
		mDanMuConnection = new ConnectDanMuServer(frame);
		if(mDanMuConnection.ConnectToDanMuServer(mRoomID.getText().trim())){//连接成功
			mIsConnectionAlive=true;
			UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "连接弹幕服务器成功", null, null, null));
		}
		else{
			mIsConnectionAlive=false;
			UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "连接弹幕服务器失败！！", null, null, null));
		}
	}
	private void CloseConnection(){
		UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "断开连接中。。。", null, null, null));
		if(mDanMuConnection!=null)
			mDanMuConnection.Close();
		mIsConnectionAlive=false;
		UpdateDanMu(new ListItemDanMu(false, false, null, "", "", "与弹幕服务器断开连接成功", null, null, null));
	}
	public void UpdateDanMu(ListItemDanMu message){
		mListItem.addElement(message);
		if(mListItem.getSize()>250){//数据过多，避免占用内存，清理掉
			mListItem.removeRange(0, mListItem.getSize()-50);
		}
		if(mIsAutoScroll){
			mMessagelastIndex = mMessageList.getModel().getSize() - 1;
			if (mMessagelastIndex >= 0) {
				mMessageList.ensureIndexIsVisible(mMessagelastIndex);
			}
		}
	}
	
	public void Lock(){
		mRoomID.setEnabled(false);
		mMessageList.setEnabled(false);
		mStartStopConnection.setVisible(false);
		mPauseAutoScroll.setVisible(false);
		mMessageList.setEnabled(false);
		mHelp.setVisible(false);
		mCloseWindow.setVisible(false);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mLockHint.setText("F10解锁");
	}
	public void UnLock(){
		mRoomID.setEnabled(true);
		mMessageList.setEnabled(true);
		mStartStopConnection.setVisible(true);
		mPauseAutoScroll.setVisible(true);
		mMessageList.setEnabled(true);
		mHelp.setVisible(true);
		mCloseWindow.setVisible(true);
		parentPanel.setEnabled(true);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mLockHint.setText("F10锁定");
	}
	//显示数据
	public void UpdateDanMu(Object message){
		ListItemDanMu danMuMessage=new ListItemDanMu();
		if(message.getClass().equals(Danmu.class)){//弹幕
			Danmu danmu = (Danmu) message;
			danMuMessage.setGift(false);
			if(danmu.mPlatform.equals(Platform.PLATFORM_Android)||danmu.mPlatform.equals(Platform.PLATFORM_Ios)){
				danMuMessage.setPhone(true);
				danMuMessage.setPhoneIcon(new ImageIcon(PandaTVDanmu.class.getResource("/pic/mobile.png")));
			}
			String userName=danmu.mNickName;
			if(Integer.parseInt(danmu.mIdentity)>=60){
				if(danmu.mIdentity.equals(User.ROLE_MANAGER)){//管理员
					danMuMessage.setSymbolAfterUserName("(管理) ");
				}
				else if(danmu.mIdentity.equals(User.ROLE_HOSTER)){//主播
					danMuMessage.setSymbolAfterUserName("(主播) ");
				}
				else if(danmu.mIdentity.equals(User.ROLE_SUPER_MANAGER)){//超管
					danMuMessage.setSymbolAfterUserName("(超管) ");
				}
			}
			else
				danMuMessage.setSymbolAfterUserName(" ");
			danMuMessage.setUserName(userName);
			danMuMessage.setMessage(danmu.mContent);
		}
		else if(message.getClass().equals(Bamboo.class)){//竹子
			Bamboo bamboo = (Bamboo) message;
			danMuMessage.setPhone(false);
			danMuMessage.setGift(true);
			danMuMessage.setUserName(bamboo.mNickName);
			danMuMessage.setSymbolAfterUserName("");
			danMuMessage.setMessage("送给主播");
			danMuMessage.setGiftNumber(bamboo.mContent);
			danMuMessage.setGiftUnit("个");
			danMuMessage.setGiftName("竹子");
		}
		else if(message.getClass().equals(Visitors.class)){//访客数量
			Visitors visitor = (Visitors) message;
			mVisitorNum.setText(visitor.mContent);
			return;
		}
		else if(message.getClass().equals(Gift.class)){//礼物
			Gift gift = (Gift) message;
			danMuMessage.setPhone(false);
			danMuMessage.setGift(true);
			danMuMessage.setUserName(gift.mNickName);
			danMuMessage.setSymbolAfterUserName("");
			danMuMessage.setMessage("送给主播");
			danMuMessage.setGiftNumber(gift.mContentCombo);
			danMuMessage.setGiftUnit("个");
			danMuMessage.setGiftName(gift.mContentName);
		}
		UpdateDanMu(danMuMessage);
	}

}
