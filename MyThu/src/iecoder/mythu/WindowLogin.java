package iecoder.mythu;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class WindowLogin implements ActionListener {
	private JTextField userId;
	private JPasswordField userPass;
	private JButton confirm, concel;
	private Container dialogPane;
	private JDialog d;
	// �Ƿ��ס����
	public static boolean rememberPass = false;

	public WindowLogin() throws Exception {
		// ����û���Ϣ
		String[] userInfo = UserInfo.getUserInfo();
		// ���ݿ���û���û���Ϣ������Ⱦ��½����
		if (userInfo[0].isEmpty()) {
			this.d = new JDialog();
			d.setTitle("MyThu ��½");
			this.dialogPane = d.getContentPane();
			this.dialogPane.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			this.dialogPane.add(new JLabel("�û���"), c);

			this.userId = new JTextField(20);
			c.gridx = 1;
			c.gridy = 0;
			this.dialogPane.add(this.userId, c);

			c.gridx = 0;
			c.gridy = 1;
			this.dialogPane.add(new JLabel("��  ��"), c);
			this.userPass = new JPasswordField(20);
			c.gridx = 1;
			c.gridy = 1;
			this.dialogPane.add(this.userPass, c);

			JRadioButton remember = new JRadioButton("��ס����");
			c.gridx = 1;
			c.gridy = 2;
			this.dialogPane.add(remember, c);
			remember.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals("��ס����")) {
						WindowLogin.rememberPass = true;
					}
				}

			});
			this.confirm = new JButton("ȷ��");
			this.concel = new JButton("�˳�");
			c.gridx = 0;
			c.gridy = 3;
			this.dialogPane.add(this.concel, c);
			c.gridx = 1;
			c.gridy = 3;
			this.dialogPane.add(this.confirm, c);
			this.confirm.addActionListener(this);
			this.concel.addActionListener(this);
			this.d.setBounds(100, 200, 300, 200);
			this.d.getRootPane().setDefaultButton(confirm);
			this.d.setVisible(true);
		} else {
			// ����Login����
			Login.userId = userInfo[0];
			Login.userPass = userInfo[1];
			WindowMain mythuMain = new WindowMain();
			if (WindowMain.login) {
				// �������õĴ洢·��
				WindowLogin.rememberPass = true;
				mythuMain.MyThuWindow(userInfo[2]);
			} else {
				JOptionPane.showMessageDialog(null, "�û������������", "MyThu ����",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ȷ��")) {
			Login.userId = this.userId.getText();
			Login.userPass = new String(this.userPass.getPassword());
			try {
				WindowMain mythu = new WindowMain();
				if (WindowMain.login) {
					// �����û�����
					if(WindowLogin.rememberPass == true) {
						UserInfo.insert(Login.userId, Login.userPass,WindowMain.rootPath);
					}
					// ���ٵ�½����
					this.d.dispose();
					// ��ȡ�γ���Ϣ�������ݿ�  
					Course.setCourses();
					// ����������
					mythu.MyThuWindow("");
				} else {
					JOptionPane.showMessageDialog(this.d, "�û������������",
							"MyThu ����", JOptionPane.WARNING_MESSAGE);
					this.userPass.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
		if (cmd.equals("�˳�")) {
			System.exit(0);
		}
	}

}
