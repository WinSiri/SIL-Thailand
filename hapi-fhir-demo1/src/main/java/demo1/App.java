package demo1;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;

import java.util.List;

public class App {

    public static void main(String[] args) {

        // Create a context
        FhirContext ctx = FhirContext.forR4();

        // Create a client
        IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

        // Read a patient with the given ID
        Patient patient = client.read().resource(Patient.class).withId("921009").execute();
        // Print the output
        System.out.println("-".repeat(40));
        System.out.println("Patient Information");
        System.out.println("-".repeat(40));
        System.out.printf("NAME: %s%n", patient.getNameFirstRep().getNameAsSingleString());
        System.out.printf("BIRTHDATE: %tF%n", patient.getBirthDate());
        
        // Read a bundle of conditions
        Bundle conditionBundle = client.search().forResource(Condition.class)
                .where(Condition.SUBJECT.hasId("921009")).returnBundle(Bundle.class).execute();
        if(conditionBundle.hasEntry()) {
            System.out.println("PROBLEM LIST:");
            List<BundleEntryComponent> entries = conditionBundle.getEntry();
            int i = 1;
            for (BundleEntryComponent ent : entries) {
                Condition c = (Condition)ent.getResource();
                System.out.printf("%3d. %-40s %-12s %n", 
                    i++,    
                    c.getCode().getText(),
                    c.getSeverity().getText());
            }
        }
        else {
            System.out.println("PROBLEM LIST: None");
        }

        // Read a bundle of encounters
        Bundle encounterBundle = client.search().forResource(Encounter.class)
                .where(Encounter.SUBJECT.hasId("921009")).returnBundle(Bundle.class).execute();
        if(encounterBundle.hasEntry()) {
            System.out.println("ENCOUNTER LIST:");
            List<BundleEntryComponent> entries = encounterBundle.getEntry();
            for (BundleEntryComponent ent : entries) {
                Encounter e = (Encounter)ent.getResource();
                System.out.printf("DATE: %tF  TYPE: %-15s  PARTICIPANT: %s %n", 
                    e.getPeriod().getStart(),
                    e.getTypeFirstRep().getText(),
                    e.getParticipantFirstRep().getIndividual().getDisplay());
            }
        }
        else {
            System.out.println("ENCOUNTER LIST: None");
        }
    }
}