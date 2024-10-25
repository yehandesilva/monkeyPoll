import {useState, useRef} from 'react';

import {Toast} from 'primereact/toast';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import {Button} from 'primereact/button';
import {validateEmail} from "../modules/validation.js";

const Register = ({setVisible}) => {

    const toast = useRef(null);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [invalidFields, setInvalidFields] = useState({
        firstName: false,
        lastName: false,
        email: false,
        password: false,
    });

    const onSubmit = () => {
        const _invalidFields = {...invalidFields};
        if (firstName && lastName && email && password) {
            if (validateEmail(email)) {
                // TODO Create user
                setVisible(false);
            } else {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Email must be in format of example@email.com'
                });
                _invalidFields.email = true;
            }
        } else {
            // Sign-in fields are empty
            if (!firstName) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'First name is required'
                });
            }
            if (!lastName) {
                toast.current.show({
                    severity: 'error',
                    life: 3000,
                    summary: 'Login Error',
                    detail: 'Last name is required'
                });
            }
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
            _invalidFields.firstName = !firstName;
            _invalidFields.lastName = !lastName;
            _invalidFields.email = !email;
            _invalidFields.password = !password;
        }
        setInvalidFields({..._invalidFields});
    }

    return (
        <>
            <Toast ref={toast}/>
            <div className="flex justify-content-center pb-6">
                <div className="flex flex-column max-w-max">
                    <div>
                        <label htmlFor="First Name" className="block mb-1 ml-1">First Name</label>
                        <InputText value={firstName} onChange={(e) => setFirstName(e.target.value)} invalid={invalidFields.firstName} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="Last Name" className="block mb-1 ml-1">Last Name</label>
                        <InputText value={lastName} onChange={(e) => setLastName(e.target.value)} invalid={invalidFields.lastName} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="email" className="block mb-1 ml-1">E-mail</label>
                        <InputText value={email} onChange={(e) => setEmail(e.target.value)} invalid={invalidFields.email} className="w-full"/>
                    </div>
                    <div className="mt-3">
                        <label htmlFor="password" className="block mb-1 ml-1">Password</label>
                        <Password value={password} toggleMask invalid={invalidFields.password}
                                  onChange={(e) => setPassword(e.target.value)}/>
                    </div>
                    <div className="flex justify-content-center mt-6">
                        <Button label="Register" icon="pi pi-user-plus" onClick={() => onSubmit()}
                                className="max-w-min"/>
                    </div>
                </div>
            </div>
        </>
    )
}

export default Register;