package im;


import java.io.Serializable;

/**
 * 身份信息
 */
public class IdentityInformation implements Serializable{
    private static final long serialVersionUID = 21125897982722447L;
    private Visitor visitor;
    private Owner owner;
    private String frame;
    public IdentityInformation() {
    }

    public Visitor getVisitor() {
        return visitor;
    }

    public void setVisitor(Visitor visitor) {
        this.visitor = visitor;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public static class Visitor implements Serializable{
        private static final long serialVersionUID = 21125897982722446L;
        private String feed_id;
        private String user_id;

        public Visitor() {
        }

        public String getFeed_id() {
            return feed_id;
        }

        public void setFeed_id(String feed_id) {
            this.feed_id = feed_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
    public static class Owner implements Serializable{
        private static final long serialVersionUID = 21125897982722445L;
        private String feed_id;
        private String company_id;

        public Owner() {
        }

        public String getFeed_id() {
            return feed_id;
        }

        public void setFeed_id(String feed_id) {
            this.feed_id = feed_id;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }
    }
}
