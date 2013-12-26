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
	public static boolean rememberPass = false;

	public WindowLogin() throws Exception {
		// 获得用户信息
		String[] userInfo = UserInfo.getUserInfo();
		if (userInfo[0].isEmpty()) {
			this.d = new JDialog();
			d.setTitle("MyThu 登陆");
			this.dialogPane = d.getContentPane();
			this.dialogPane.setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();
			c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = 0;
			c.gridy = 0;
			this.dialogPane.add(new JLabel("用户名"), c);

			this.userId = new JTextField(20);
			c.gridx = 1;
			c.gridy = 0;
			this.dialogPane.add(this.userId, c);

			c.gridx = 0;
			c.gridy = 1;
			this.dialogPane.add(new JLabel("密  码"), c);
			this.userPass = new JPasswordField(20);
			c.gridx = 1;
			c.gridy = 1;
			this.dialogPane.add(this.userPass, c);

			JRadioButton remember = new JRadioButton("记住密码");
			c.gridx = 1;
			c.gridy = 2;
			this.dialogPane.add(remember, c);
			remember.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (e.getActionCommand().equals("记住密码")) {
						rememberPass = true;
					}
				}

			});
			this.confirm = new JButton("确定");
			this.concel = new JButton("退出");
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
			// 设置Login属性
			Login.userId = userInfo[0];
			Login.userPass = userInfo[1];
			WindowMain mythu = new WindowMain();
			if (WindowMain.login) {
				mythu.MyThuWindow(userInfo[2]);
			} else {
				JOptionPane.showMessageDialog(null, "用户名或密码错误", "MyThu 错误",
						JOptionPane.WARNING_MESSAGE);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if (cmd.equals("确定")) {
			Login.userId = this.userId.getText();
			Login.userPass = new String(this.userPass.getPassword());
			try {
				WindowMain mythu = new WindowMain();
				if (WindowMain.login) {
					this.d.dispose();
					mythu.MyThuWindow("");
				} else {
					JOptionPane.showMessageDialog(this.d, "用户名或密码错误",
							"MyThu 错误", JOptionPane.WARNING_MESSAGE);
					userPass.setText("");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			return;
		}
		if (cmd.equals("退出")) {
			System.exit(0);
		}
	}

}
