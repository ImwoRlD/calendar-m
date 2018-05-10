package cc.yy.calendar.activity;

public class UserData {
    private String userName;                  //用户名
    private String userPwd;                   //用户密码
    private int userId;                       //用户ID号
    private String pwdKeywords;               //密码关键字
    public int pwdresetFlag=0;
    //获取用户名
            public String getUserName() {             //获取用户名
                return userName;
            }
    //设置用户名
            public void setUserName(String userName) {  //输入用户名
                this.userName = userName;
            }
    //获取用户密码
            public String getUserPwd() {                //获取用户密码
                return userPwd;
            }
    //设置用户密码
            public void setUserPwd(String userPwd) {     //输入用户密码
                this.userPwd = userPwd;
            }
    //获取用户id
            public int getUserId() {                   //获取用户ID号
                return userId;
            }
    //设置用户id
            public void setUserId(int userId) {       //设置用户ID号
                this.userId = userId;
            }

             public String getPwdKeywords(){         //获取密码关键字
                 return pwdKeywords;
             }
            //设置密码关键字

            public void setPwdKeywords(String pwdKeywords){
                this.pwdKeywords = pwdKeywords;
            }

    public UserData(String userName, String userPwd,String pwdKeywords) {  //这里只采用用户名和密码
                super();
                this.userName = userName;
                this.userPwd = userPwd;
                this.pwdKeywords = pwdKeywords;
            }

}
