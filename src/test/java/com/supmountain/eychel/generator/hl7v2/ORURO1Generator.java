package com.supmountain.eychel.generator.hl7v2;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v281.datatype.CWE;
import ca.uhn.hl7v2.model.v281.datatype.XPN;
import ca.uhn.hl7v2.model.v281.group.ORU_R01_OBSERVATION;
import ca.uhn.hl7v2.model.v281.group.ORU_R01_ORDER_OBSERVATION;
import ca.uhn.hl7v2.model.v281.group.ORU_R01_PATIENT;
import ca.uhn.hl7v2.model.v281.group.ORU_R01_PATIENT_RESULT;
import ca.uhn.hl7v2.model.v281.message.ORU_R01;
import ca.uhn.hl7v2.model.v281.segment.MSH;
import ca.uhn.hl7v2.model.v281.segment.OBR;
import ca.uhn.hl7v2.model.v281.segment.OBX;
import ca.uhn.hl7v2.model.v281.segment.PID;

public class ORURO1Generator {
	public Message generate() throws Exception {
		ORU_R01 message = new ORU_R01();

		message.initQuickstart("ORU", "R01", "P");

		MSH msh = message.getMSH();
		msh.getSendingFacility().getNamespaceID().setValue("RIH");

		ORU_R01_PATIENT_RESULT result = message.getPATIENT_RESULT();
		ORU_R01_PATIENT patient = result.getPATIENT();

		PID pid = patient.getPID();

		{
			XPN xpn = pid.getPatientName(0);

			xpn.getFamilyName().getSurname().setValue("DOE");
			xpn.getGivenName().setValue("JOHN");
		}

		ORU_R01_ORDER_OBSERVATION obs = result.getORDER_OBSERVATION(0);
		OBR obr = obs.getOBR();
		obr.getFillerField1().setValue("ORD");

		ORU_R01_OBSERVATION obxr = obs.getOBSERVATION(0);
		OBX obx = obxr.getOBX();
		obx.getValueType().setValue("CWE");
		obx.getSetIDOBX().setValue("9");

		{
			CWE cwe = new CWE(message);
			cwe.getIdentifier().setValue("80146002");
			cwe.getText().setValue("Appendectomy");
			cwe.getNameOfCodingSystem().setValue("SCT");
			obx.getObservationValue(0).setData(cwe);
		}

		obx.getObservationIdentifier().getIdentifier().setValue("29300-1");
		obx.getObservationIdentifier().getText().setValue("Procedure Performed");
		obx.getObservationIdentifier().getNameOfCodingSystem().setValue("LN");

		return message;
	}
}
