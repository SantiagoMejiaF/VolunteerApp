export interface Volunteer {
    userId: number;
    visibility: string;
    personalInformation: {
        identificationCard: string;
        phoneNumber: string;
        address: string;
        bornDate: string;
        personalDescription: string;
    };
    volunteeringInformation: {
        availabilityDaysList: string[];
        interestsList: string[];
        skillsList: string[];
    };
    emergencyInformation: {
        emergencyContactFirstName: string;
        emergencyContactLastName: string;
        emergencyContactRelationship: string;
        emergencyContactPhone: string;
        emergencyContactEmail: string;
    };
}