package com.me.core.domain.entities;//package com.me.core.domain.entities;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Entity
//@Table(name = "chosen_websites")
//public class ChosenWebsite extends Website implements Serializable {
////    @Id
////    @GeneratedValue(strategy = IDENTITY)
////    @Column(name = "entry_id")
////    private long id;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="dataset_id")
//    private DataSet dataSet;
//
//    @Column(name = "is_for_learning")
//    private boolean isForLearning;
//
//    public ChosenWebsite(String url, Category category, Blacklist blacklist,
//                         DataSet dataSet, boolean isForLearning) {
//        super(url, category, blacklist);
//        this.dataSet = dataSet;
//        this.isForLearning = isForLearning;
//    }
//
////    public long getId() {
////        return id;
////    }
//
////    public void setId(long dataSetId) {
////        this.id = dataSetId;
////    }
//
//    public boolean isForLearning() {
//        return isForLearning;
//    }
//
//    public void setForLearning(boolean forLearning) {
//        isForLearning = forLearning;
//    }
//
//    public DataSet getDataSet() {
//        return dataSet;
//    }
//
//    public void setDataSet(DataSet dataSet) {
//        this.dataSet = dataSet;
//    }
//
//    public Website getWebsite() {
//        Website website = new Website(super.getUrl(), super.getCategory(), super.getBlacklist());
//        website.setWebsiteID(super.getWebsiteID());
//        return website;
//    }
//
//    @Override
//    public String toString() {
//        return "ChosenWebsite{" +
////                "id=" + id +
//                ", dataSet=" + dataSet +
//                ", isForLearning=" + isForLearning +
//                ", website=" + super.toString() +
//                '}';
//    }
//
////    @Override
////    public boolean equals(Object o) {
////        if (this == o) return true;
////        if (o == null || getClass() != o.getClass()) return false;
////        if (!super.equals(o)) return false;
////
////        ChosenWebsite that = (ChosenWebsite) o;
////
////        if (id != that.id) return false;
////        if (isForLearning != that.isForLearning) return false;
////        return dataSet.equals(that.dataSet);
////    }
////
////    @Override
////    public int hashCode() {
////        int result = super.hashCode();
////        result = 31 * result + (int) (id ^ (id >>> 32));
////        result = 31 * result + dataSet.hashCode();
////        result = 31 * result + (isForLearning ? 1 : 0);
////        return result;
////    }
//}
