/* Corine Jacobs
   10001326
   Corine_J@MSN.com */

/*
  This class creates instances of artrecords to be shown in ResultsActivity.java */

package nl.mprog.mume.Classes;

public class Artrecord {

    private String title;
    private String principalmaker;
    private String dating;
    private String materials;
    private String objectnumber;

    // Constructor
    public Artrecord(String mtitle, String mprincipalmaker, String mdating, String mmaterials, String mobjectnumber){

        title = mtitle;
        principalmaker = mprincipalmaker;
        dating = mdating;
        materials = mmaterials;
        objectnumber = mobjectnumber;

    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrincipalmaker() {
        return principalmaker;
    }

    public void setPrincipalmaker(String principalmaker) {
        this.principalmaker = principalmaker;
    }

    public String getDating() {
        return dating;
    }

    public void setDating(String dating) {
        this.dating = dating;
    }

    public String getMaterials() {
        return materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public String getObjectnumber() {
        return objectnumber;
    }

    public void setObjectnumber(String objectnumber) {
        this.objectnumber = objectnumber;
    }
}
