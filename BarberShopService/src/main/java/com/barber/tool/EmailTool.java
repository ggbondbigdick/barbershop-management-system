package com.barber.tool;

import org.springframework.stereotype.Component;

/**
 * ClassName: EmailTool
 * Description:
 */
@Component
public class EmailTool {

    // @Value("${spring.mail.useraliasname}")
    // private String from;
    //
    // @Resource
    // private JavaMailSender javaMailSender;
    //
    //
    // /**
    //  * 简单文本邮件
    //  *
    //  * @param recipient 收件人
    //  *        subject 主题
    //  *        content 内容
    //  */
    // @Async
    // public void sendSimpleMail(String recipient, String subject, String content) {
    //     // 创建邮件对象
    //     javaMailSender.send(mimeMessage -> {
    //         // 设置发件人、收件人、主题、内容
    //         mimeMessage.setFrom(from);
    //         mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, recipient);
    //         mimeMessage.setSubject(subject);
    //         mimeMessage.setText(content);
    //     });
    // }
    //
    //
    // /**
    //  * HTML 邮件
    //  *
    //  * @param recipient
    //  * @param subject
    //  * @param content
    //  */
    // public void sendHtmlMail(String recipient, String subject, String content) {
    //     // 创建邮件对象
    //     javaMailSender.send(mimeMessage -> {
    //         mimeMessage.setFrom(from);
    //         mimeMessage.setRecipients(javax.mail.Message.RecipientType.TO, recipient);
    //         mimeMessage.setSubject(subject);
    //         mimeMessage.setContent(content, "text/html;charset=UTF-8");
    //     });
    // }
    //
    //
    // /**
    //  * HTML 附件邮件
    //  * @param recipient
    //  * @param subject
    //  * @param content
    //  * @param filePath
    //  */
    // public void sendAttachmentMail(String recipient, String subject, String content, String filePath) {
    //     try {
    //         MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    //         MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
    //         helper.setTo(recipient);
    //         helper.setSubject(subject);
    //         helper.setText(content, true);
    //         helper.setFrom(from);
    //
    //         // 添加附件
    //         FileSystemResource file = new FileSystemResource(new File(filePath));
    //         String fileName = file.getFilename();
    //         helper.addAttachment(fileName, file);
    //
    //         javaMailSender.send(mimeMessage);
    //     } catch (javax.mail.MessagingException e) {
    //         e.printStackTrace();
    //         throw new RuntimeException("邮件发送失败");
    //     }
    // }

}
