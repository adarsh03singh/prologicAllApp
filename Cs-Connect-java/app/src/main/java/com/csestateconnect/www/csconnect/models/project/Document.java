package com.csestateconnect.www.csconnect.models.project;

import java.io.Serializable;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document implements Serializable, Parcelable
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("document_url")
    @Expose
    private String documentUrl;
    @SerializedName("document_type")
    @Expose
    private DocumentType documentType;
    public final static Parcelable.Creator<Document> CREATOR = new Creator<Document>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        public Document[] newArray(int size) {
            return (new Document[size]);
        }

    }
            ;
    private final static long serialVersionUID = 1982331463842311309L;

    protected Document(Parcel in) {
        this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.documentUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.documentType = ((DocumentType) in.readValue((DocumentType.class.getClassLoader())));
    }

    public Document() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(documentUrl);
        dest.writeValue(documentType);
    }

    public int describeContents() {
        return 0;
    }

}