//package com.me.core.domain.entities;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Entity
//@Table(name = "aml_files")
//public class AmlFile implements Serializable{
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "entry_id")
//    private long entry_id;
//
//    @Column(name = "feature_val", columnDefinition="TEXT")
//    private String feature;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="exp_number")
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
//    public String getFeature() {
//        return feature;
//    }
//
//    public void setFeature(String feature) {
//        this.feature = feature;
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
//        return "AML{" +
//                "entry_id=" + entry_id +
//                ", feature='" + feature + '\'' +
//                ", experiment=" + experiment.getExperimentNumber() +
//                '}';
//    }
//}
