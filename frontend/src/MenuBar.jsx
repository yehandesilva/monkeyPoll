import {useState} from 'react';

import {Button} from 'primereact/button';

import LoginDialog from "./welcome/LoginDialog.jsx";

const MenuBar = () => {

    const [loginVisible, setLoginVisible] = useState(false);

    return (
        <>
            <LoginDialog visible={loginVisible} setVisible={setLoginVisible}/>
            <Button label="Login" icon="pi pi-sign-in" size="small" className="absolute top-0 right-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setLoginVisible(true)}/>
        </>

    )
}

export default MenuBar;