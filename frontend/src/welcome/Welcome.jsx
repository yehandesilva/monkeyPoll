import {useState} from 'react';

import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';

const Welcome = () => {
    const [code, setCode] = useState("");

    return (
        <div className="h-screen w-full relative" style={{backgroundColor: '#014F5E'}}>
            <Button label="Login" icon="pi pi-sign-in" size="small" className="absolute top-0 right-0 m-4" style={{boxShadow: "none"}} />

            <div className="flex flex-column justify-content-center align-items-center gap-4 h-full">
                <img src="monkeyPollLogoWithWhiteText.png" alt="MonkeyPoll Logo"
                     style={{width: '90%', maxWidth: '1080px', height: 'auto'}}/>
                <div className="flex gap-2">
                    <InputText value={code} onChange={(e) => setCode(e.target.value)} placeholder="Enter Code"
                               className="p-inputtext-lg"/>
                    <Button icon="pi pi-arrow-right" size="large" style={{boxShadow: "none"}}/>
                </div>
            </div>
        </div>
    )
}

export default Welcome;