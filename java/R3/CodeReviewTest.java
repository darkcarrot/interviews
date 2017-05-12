package chapter1;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

// Imagine you are a peer of the developer who committed this (syntactically correct) Java code and asked you to review
// their pull request. You work on the same product but are not familiar with this piece of work or its associated
// requirements.
//
// Please use Java comments for your review feedback, putting them on separate lines around the code. Do not modify the
// code itself.

public class CodeReviewTest {

    //TODO: Need neither volatile nor Integer
    //TODO: Add an access modifier
    volatile Integer totalAge = 0;

    //TODO: access modifier for the constructor
    CodeReviewTest(PersonDatabase<Person> personPersonDatabase) {
        Person[] persons = null;
        try {
            persons = personPersonDatabase.getAllPersons();
        } catch (IOException e) {
            //TODO: Don't have an empty catch block
        }

        //TODO: checked LinkedList: new LinkedList<>(), like you did below
        List<Person> personsList = new LinkedList();

        //TODO: what if persons is null?
        //TODO: Use collection operation, such as personsList.addAll(Arrays.asList(persons).subList(0, persons.length + 1));
        for (int i = 0; i <= persons.length; i++) {
            personsList.add(persons[i]);
        }

        //TODO: Be careful with parallelStream
        //TODO: this can be simplify to: totalAge = personsList.stream().mapToInt(Person::getAge).sum();
        personsList.parallelStream().forEach(person -> {
            totalAge += person.getAge();
        });

        List<Person> males = new LinkedList<>();

        for (Person person : personsList) {
            //TODO: use a getter
            switch (person.gender) {
                case "Female": personsList.remove(person); //TODO: you sure?
                case "Male"  : males.add(person);
            }
        }
        //TODO: the above can be simplify as below
        //final int total = personsList.size();
        //final long male = personsList.stream().filter(person -> person.gender.equals("Male")).count();
        ////Assume only two sexes
        //final long female = (long) total - male;

        //TODO: prefer using a logger
        System.out.println("Total age =" + totalAge);
        //TODO: the personList will contain only males, as you remove all females!
        System.out.println("Total number of females =" + personsList.size());
        System.out.println("Total number of males =" + males.size());

        //TODO: after all, do you want to put all these in the constructor? Maybe in separated methods
    }

}

//TODO: put interface in another file
//TODO: or use suitable access modifier
class Person {

    //TODO: do you want to make a Person immutable? Think about it
    private int age;
    //TODO: getter/setter for firstName/lastName
    private String firstName;
    private String lastName;
    //TODO: use a getter for gender
    //TODO: a setter for gender
    //TODO: use an Enum class instead of String
    String gender;

    public Person(int age, String firstName, String lastName) {
        this.age = age;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    @Override
    //TODO: overwrite the the hashCode method
    //TODO: are you sure you only check the lastName?
    public boolean equals(Object obj) {
        //TODO: Use String::equals, not ==
        return this.lastName == ((Person)obj).lastName;
    }

}

//TODO: put interface in another file?
//TODO: if you use generic, a better design is required
//Such as: interface Database<E>: { Collection<E> getAll;}
interface PersonDatabase<E> {

    Person[] getAllPersons() throws IOException;

}

