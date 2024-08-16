export interface Volunteer {
    userId: number;
    personalInformation: {
        identificationCard: string;
        phoneNumber: string;
        address: string;
        birthDate: string;
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
