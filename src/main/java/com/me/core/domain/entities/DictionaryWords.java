//package com.me.core.domain.entities;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//import static javax.persistence.GenerationType.IDENTITY;
//
//@Entity
//@Table(name = "dictionary_words")
//public class DictionaryWords implements Serializable {
//    @Id
//    @GeneratedValue(strategy = IDENTITY)
//    @Column(name = "entry_id")
//    private long entry_id;
//
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name="experiment_num")
//    private Experiment experiment;
//
//    @Column(name = "dict_word", columnDefinition="TEXT")
//    private String word;
//
//    public long getEntry_id() {
//        return entry_id;
//    }
//
//    public void setEntry_id(long entry_id) {
//        this.entry_id = entry_id;
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
//    public String getWord() {
//        return word;
//    }
//
//    public void setWord(String word) {
//        this.word = word;
//    }
//
//    @Override
//    public String toString() {
//        return "DictionaryWords{" +
//                "entry_id=" + entry_id +
//                ", experiment=" + experiment.getExperimentNumber() +
//                ", word='" + word + '\'' +
//                '}';
//    }
//}
