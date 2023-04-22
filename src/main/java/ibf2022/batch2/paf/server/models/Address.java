package ibf2022.batch2.paf.server.models;

import org.bson.Document;

public class Address {
    private String building;
    private String street;
    private String zipcode;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address [building=" + building + ", street=" + street + ", zipcode=" + zipcode + "]";
    }

    public static Address convertFromDocument(Document d) {
        Address a = new Address();
        a.setBuilding(d.getString("building"));
        a.setStreet(d.getString("street"));
        a.setZipcode(d.getString("zipcode"));
        return a;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}
