package iecoder.mythu;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Login implements ActionListener {
	private static final boolean shouldFill = true;
	private static final boolean shouldWeightX = false;
	JTextField userid;
	JPasswordField userpass;
	public static String name;
	public static String pass;
	JButton confirm, concel;
	Container dialogPane;
	JDialog d;
	public static boolean rememberPass = false;

	public Login() throws Exception {
		String[] user = Data.verify();
		if (user[0].isEmpty()) {
			this.d = new JDialog();
			d.setTitle("MyThu ��½");
			this.dialogPane = d.getContentPane();
			this.dialogPane.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			if (shouldFill) {
				c.fill = GridBagConstraints.HORIZONTAL;
			}
			if (shouldWeightX) {
				c.weightx = 0.5;
			}
			c.gridx = 0;
			c.gridy = 0;
			this.dialogPane.add(new JLabel("�û���"), c);

			this.userid = new JTextField(20);
			c.gridx = 1;
			c.gridy = 0;
			this.dialogPane.add(this.userid, c);

			c.gridx = 0;
			c.gridy = 1;
			this.dialogPane.add(new JLabel("��  ��"), c);
			this.userpass = new JPasswordField(20);
			c.gridx = 1;
			c.gridy = 1;
			this.dialogPane.add(this.userpass, c);

			JRadioButton remember = new JRadioButton("��ס����");
			c.gridx = 1;
			c.gridy = 2;
			this.dialogPane.add(remember, c);
			remember.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals("��ס����")) {
						rememberPass = true;
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
			MyThu mythu = new MyThu(user[0], user[1]);
			if (MyThu.login) {
				mythu.MyThuWindow(user[2]);
			} else {
				JOptionPane.showMessageDialog(null, "�û������������", "MyThu ����",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("ȷ��")) {
			Login.name = this.userid.getText();
			Login.pass = new String(this.userpass.getPassword());
			try {
				MyThu mythu = new MyThu(name, pass);
				if (MyThu.login) {
					this.d.dispose();
					mythu.MyThuWindow("");
				} else {
					JOptionPane.showMessageDialog(this.d, "�û������������",
							"MyThu ����", JOptionPane.WARNING_MESSAGE);
					userpass.setText("");
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
