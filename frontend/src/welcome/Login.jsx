import {useState, useRef} from 'react';

import {Toast} from 'primereact/toast';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import {Button} from 'primereact/button';
import {validateEmail} from "../modules/validation.js";

const Login = ({setVisible}) => {

    const toast = useRef(null);
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const onSubmit = () => {
        if (email && password) {
            // Validate email format
            if (validateEmail(email)) {
                // TODO Sign-in user
                setVisible(false);
            } else {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Email must be in format of example@email.com'
                });
            }
        } else {
            // Email or password fields are empty
            if (!email) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Email is required'
                });
            }
            if (!password) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Password is required'
                });
            }
        }
    }

    return (
        <>
            <Toast ref={toast}/>
            <div className="flex justify-content-center pb-6">
                <div className="flex flex-column max-w-max">
                    <div>
                        <label htmlFor="email" className="block mb-1 ml-1">E-mail</label>
                        <InputText value={email} onChange={(e) => setEmail(e.target.value)} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="password" className="block mb-1 ml-1">Password</label>
                        <Password value={password} toggleMask feedback={false}
                                  onChange={(e) => setPassword(e.target.value)}/>
                    </div>
                    <div className="flex justify-content-center mt-6">
                        <Button label="Login" icon="pi pi-sign-in" onClick={() => onSubmit()} className="max-w-min"/>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Login;