from fhirclient.client import FHIRServer
from fhirclient.models.patient import Patient

server = FHIRServer(None, "http://hapi.fhir.org/baseDstu3")
p = Patient.read("2716055", server)
print("-" * 40)
print("Patient Information")
print("-" * 40)
print(f"NAME: {p.name[0].text}")
print(f"BIRTHDATE: {p.birthDate.date.isoformat()}")

from fhirclient.models.condition import Condition

conditions = Condition.where({"subject": "2716055"}).perform_resources(server)
if not conditions:
    print("PROBLEM LIST: None")
else:
    print("PROBLEM LIST:")
    i = 1
    for cond in conditions:
        print(f"{i:>3d}. {cond.code.text:<40s} Severity: {cond.severity.text:<12s}")
        i += 1

from fhirclient.models.encounter import Encounter

encounters = Encounter.where({"subject": "2716055"}).perform_resources(server)
if not encounters:
    print("ENCOUNTER LIST: None")
else:
    print("ENCOUNTER LIST:")
    for enct in encounters:
        print(
            f"- Date: {enct.period.start.date.date().isoformat():<12s} Type: {enct.type[0].text:<15s} Reason: {enct.reason[0].text:<20} Participant: {enct.participant[0].individual.display}")
