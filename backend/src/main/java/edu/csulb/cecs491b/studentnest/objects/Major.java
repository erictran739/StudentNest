package edu.csulb.cecs491b.studentnest.objects;

public enum Major {

    COMPUTER_SCIENCE("Computer Science"),
    COMPUTER_ENGINEERING("Computer Engineering"),
    ELECTRICAL_ENGINEERING("Electrical Engineering");

    private final String major;

    Major(String major){
        this.major = major;
    }

    @Override
    public String toString() {
        return this.major;
    }

}
