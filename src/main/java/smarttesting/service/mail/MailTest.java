package smarttesting.service.mail;

import org.springframework.core.io.ClassPathResource;

import java.io.File;

public class MailTest {

    public static void main(String[] args) throws Exception {
        Mail sms = Mail.createESmtpMailSender("smtp.*.qq.com","<*@*.com>","*","****");
        sms.addLogManager(new LogPrinter());
        ClassPathResource resource = new ClassPathResource("log4j.xml");
        String img = resource.getFile().getParent() + "/frontend/f/favicon.ico";
        String file = resource.getFile().getParent() + "/frontend/f/zdfile";
        sms.sendHtmlMail(new String[]{"real<*@*.com>"},
                "标题",
                "这是一封测试邮件。<img width=20 height=20 src='" + img + "'>", new File[]{new File(file)});
        System.out.println("邮件发送成功。");
    }

    static class LogPrinter implements MailLog {
        @Override
        public void info(String info) {
            System.out.println(info);
        }
    }
}


