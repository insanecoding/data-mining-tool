//package com.me.core.domain.entities;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Entity
//@Table(name = "local_tfidf")
//public class Local_tfidf implements Serializable {
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "entry_id")
//    private long entry_id;
//
//    @OneToOne
//    @JoinColumn(name="category_id")
//    private Category category;
//
//    @Column(name = "term", columnDefinition="TEXT")
//    private String term;
//
//    @Column(name = "value", columnDefinition="TEXT")
//    private String value;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="exp_num")
//    private Experiment experiment;
//
//    public long getEntry_id() {
//        return entry_id;
//    }
//
//    public void setEntry_id(long entry_id) {
//        this.entry_id = entry_id;
//    }
//
//    public Category getCategory() {
//        return category;
//    }
//
//    public void setCategory(Category category) {
//        this.category = category;
//    }
//
//    public String getTerm() {
//        return term;
//    }
//
//    public void setTerm(String term) {
//        this.term = term;
//    }
//
//    public String getValue() {
//        return value;
//    }
//
//    public void setValue(String value) {
//        this.value = value;
//    }
//
//    public Experiment getExperiment() {
//        return experiment;
//    }
//
//    public void setExperiment(Experiment experiment) {
//        this.experiment = experiment;
//    }
//
//    @Override
//    public String toString() {
//        return "Local_tf{" +
//                "entry_id=" + entry_id +
//                ", category=" + category.getCategoryName() +
//                ", term='" + term + '\'' +
//                ", value='" + value + '\'' +
//                ", experiment=" + experiment.getExperimentNumber() +
//                '}';
//    }
//}
