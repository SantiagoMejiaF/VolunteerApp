export interface Organization {
    userId: number;
    institutionalInformation: {
        nit: string;
        foundationName: string;
        website: string;
    };
    contactInformation: {
        phoneNumber: string;
        email: string;
        address: string;
    };
}
