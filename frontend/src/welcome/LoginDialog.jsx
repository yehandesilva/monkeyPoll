import {useState} from 'react';

import {Dialog} from 'primereact/dialog';
import {Button} from 'primereact/button';

const LoginDialog = ({visible, setVisible}) => {

    const [isLogin, setIsLogin] = useState(true);

    return (
        <>
            <Dialog visible={visible} style={{width: '75vw'}} onHide={() => {
                setVisible(false);
            }}>
                <div className="flex flex-column gap-2">
                    <div className="flex flex-column justify-content-center align-items-center gap-4 h-full">
                        <img src="monkeyPollLogoWithWhiteText.png" alt="MonkeyPoll Logo"
                             style={{width: '80%', maxWidth: '400px', height: 'auto'}}/>
                    </div>
                    <div className="flex justify-content-center align-items-center">
                        <span>{(isLogin) ? "Don't have an account?" : "Already have an account?"}</span>
                        <Button text onClick={() => setIsLogin(!isLogin)} className="px-2"
                                style={{boxShadow: "none"}}>{(isLogin) ? "Create!" : "Login!"}</Button>
                    </div>
                </div>
            </Dialog>
        </>
    )
}

export default LoginDialog;