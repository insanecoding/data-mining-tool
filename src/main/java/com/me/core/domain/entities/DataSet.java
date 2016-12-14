package com.me.core.domain.entities;//package com.me.core.domain.entities;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Objects;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Entity
//@Table(name = "dataset")
//public class DataSet implements Serializable {
//
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "dataset_id")
//    private Long dataSetId;
//
//    @Column(name = "name", unique = true)
//    private String name;
//
//    @Column(name = "description")
//    private String description;
//
//    public DataSet(String name, String description) {
//        this.name = name;
//        this.description = description;
//    }
//
//    public DataSet() {}
//
//    public Long getDataSetId() {
//        return dataSetId;
//    }
//
//    public void setDataSetId(Long dataSetId) {
//        this.dataSetId = dataSetId;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    @Override
//    public String toString() {
//        return "DataSet{" +
//                "dataSetId=" + dataSetId +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                '}';
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        DataSet dataSet = (DataSet) o;
//
//        if (!Objects.equals(dataSetId, dataSet.dataSetId)) return false;
//        if (!name.equals(dataSet.name)) return false;
//        return description.equals(dataSet.description);
//    }
//
//    @Override
//    public int hashCode() {
//        int result = (int) (dataSetId ^ (dataSetId >>> 32));
//        result = 31 * result + name.hashCode();
//        result = 31 * result + description.hashCode();
//        return result;
//    }
//}
