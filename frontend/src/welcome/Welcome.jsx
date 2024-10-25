import {useState} from 'react';

import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';

const Welcome = () => {
    const [code, setCode] = useState((window.location.pathname).toString().substring(1));

    return (
        <div className="flex flex-column justify-content-center align-items-center gap-4 h-full">
            <img src="monkeyPollLogoWithWhiteText.png" alt="MonkeyPoll Logo"
                 style={{width: '90%', maxWidth: '1080px', height: 'auto'}}/>
            <div className="flex gap-2">
                <InputText value={code} onChange={(e) => setCode(e.target.value)} placeholder="Enter Code"
                           className="p-inputtext-lg"/>
                <Button icon="pi pi-arrow-right" size="large" style={{boxShadow: "none"}}/>
            </div>
        </div>
    )
}

export default Welcome;