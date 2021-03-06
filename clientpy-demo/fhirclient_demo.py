from fhirclient.client import FHIRServer
from fhirclient.models.patient import Patient

server = FHIRServer(None, "http://hapi.fhir.org/baseR4")
p = Patient.read("921009", server)
print("-" * 40)
print("Patient Information")
print("-" * 40)
print(f"NAME: {p.name[0].text}")
print(f"BIRTHDATE: {p.birthDate.date.isoformat()}")

from fhirclient.models.condition import Condition

# from fhirclient.models.bundle import Bundle

# conditionBundle = Condition.where( { "subject": "921009" } ).perform( server )
# if conditionBundle.entry is None:
conditions = Condition.where({"subject": "921009"}).perform_resources(server)
if not conditions:
    print("PROBLEM LIST: None")
else:
    print("PROBLEM LIST:")
    i = 1
    # for entry in conditionBundle.entry:
    for cond in conditions:
        # cond = entry.resource
        print(f"{i:>3d}. {cond.code.text:<40s} Severity: {cond.severity.text:<12s}")
        i += 1
