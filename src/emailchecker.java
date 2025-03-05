import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailChecker {
    // IMAP settings (example for Gmail)
    private static final String HOST = "imap.gmail.com";
    private static final String USERNAME = "your_email@gmail.com";
    private static final String PASSWORD = "your_app_password";

    // Delete-trigger keywords
    private static final String[] DELETE_KEYWORDS = {
        "we have filled the spot",
        "we have filled a spot",
        "position has been filled",
        "we found a different applicant"
    };

    public static void main(String[] args) {
        checkEmailAndDeleteJobs();
    }

    public static void checkEmailAndDeleteJobs() {
        // 1) Set mail properties
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps"); // We'll use IMAPS
        props.put("mail.imaps.host", HOST);
        props.put("mail.imaps.port", "993");
        // If needed, you can set additional SSL props here

        Session session = Session.getDefaultInstance(props, null);

        Store store = null;
        Folder inbox = null;
        try {
            // 2) Connect to the store
            store = session.getStore("imaps");
            store.connect(HOST, USERNAME, PASSWORD);

            // 3) Open the INBOX folder
            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            // (Optional) If you only want unread messages:
            // Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            // If you want *all* messages:
            Message[] messages = inbox.getMessages();

            System.out.println("[INFO] Found " + messages.length + " messages in INBOX.");

            // 4) Iterate through each message
            for (Message msg : messages) {
                // We can skip truly old emails if needed, but let's just check them all.
                if (!(msg instanceof MimeMessage)) {
                    continue;
                }

                MimeMessage m = (MimeMessage) msg;
                String subject = (m.getSubject() == null) ? "" : m.getSubject();
                Address[] froms = m.getFrom();
                String fromEmail = (froms != null && froms.length > 0) 
                                   ? froms[0].toString() 
                                   : "unknown@unknown.com";

                // 5) Get the content (body). We’ll collect text from all parts if multipart
                StringBuilder bodySB = new StringBuilder();
                Object content = m.getContent();
                if (content instanceof Multipart) {
                    Multipart multiPart = (Multipart) content;
                    for (int i = 0; i < multiPart.getCount(); i++) {
                        BodyPart bp = multiPart.getBodyPart(i);
                        // If text/plain or text/html, read it
                        if (bp.isMimeType("text/plain") || bp.isMimeType("text/html")) {
                            bodySB.append(((MimeBodyPart) bp).getContent().toString());
                            bodySB.append("\n");
                        }
                    }
                } else if (content instanceof String) {
                    // If it’s a simple message with no multipart
                    bodySB.append((String) content);
                }
                String emailBody = bodySB.toString();

                // Convert to lowercase for simpler matching
                String combinedText = (subject + "\n" + emailBody).toLowerCase();

                // 6) Check if any "delete" keyword is present
                boolean shouldDelete = false;
                for (String kw : DELETE_KEYWORDS) {
                    if (combinedText.contains(kw.toLowerCase())) {
                        shouldDelete = true;
                        break;
                    }
                }

                // 7) If job is filled => parse out the company name => call DeleteRow
                if (shouldDelete) {
                    System.out.println("\n[INFO] Found an email about a filled position!");
                    System.out.println("       Subject: " + subject);
                    System.out.println("       From:    " + fromEmail);

                    // Try to parse the company name from the from-address or subject
                    String companyName = parseCompanyName(fromEmail);
                    if (companyName != null && !companyName.isEmpty()) {
                        System.out.println("[INFO] Attempting to delete job from DB for company: " + companyName);
                        // Call your existing Java class
                        DeleteRow deleter = new DeleteRow();
                        deleter.delete(companyName);
                    } else {
                        System.out.println("[WARNING] Could not parse a valid company name from email. No DB delete performed.");
                    }

                    // Mark message as SEEN, or whatever flags you prefer
                    msg.setFlag(Flags.Flag.SEEN, true);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 8) Cleanup
            if (inbox != null && inbox.isOpen()) {
                try {
                    inbox.close(false); // false -> don’t expunge
                } catch (MessagingException e) {
                    // ignore
                }
            }
            if (store != null) {
                try {
                    store.close();
                } catch (MessagingException e) {
                    // ignore
                }
            }
        }
    }

    // ----------------------------------------------------------
    // Helper function to parse "company name" from the from-address
    // E.g. "Amazon HR <hr@amazon.com>" -> "amazon"
    // ----------------------------------------------------------
    private static String parseCompanyName(String fromAddress) {
        // Simple approach: find text after '@' and before '.'
        // e.g. "hr@amazon.com" -> "amazon"
        // e.g. "hr@google.co.in" -> "google"
        // Then capitalizes first letter: "Amazon"
        Pattern pattern = Pattern.compile("@(.*?)\\.");
        Matcher matcher = pattern.matcher(fromAddress.toLowerCase());
        if (matcher.find()) {
            String domainCore = matcher.group(1); // e.g. "amazon"
            return capitalize(domainCore);
        }
        return null;
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }
}
