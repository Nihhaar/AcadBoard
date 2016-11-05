package iitb.nihhaar.acadboard;

/**
 * Created by Nihhaar on 11/2/2016.
 */

class Card {

        private String line1;
        private String line2;
        private String description;

        public Card(String line1, String line2, String description) {
            this.line1 = line1;
            this.line2 = line2;
            this.description = description;
        }

        public String getLine1() {
            return line1;
        }

        public String getLine2() {
            return line2;
        }

        public String getDescription(){return description;}

}
