/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: paulo.abrantes@qub-it.com
 *
 * 
 * This file is part of FenixEdu fenixedu-ulisboa-ldapIntegration.
 *
 * FenixEdu fenixedu-ulisboa-ldapIntegration is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu fenixedu-ulisboa-ldapIntegration is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu fenixedu-ulisboa-ldapIntegration.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.qubit.solution.fenixedu.integration.ldap.service.task;

import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.scheduler.custom.CustomTask;

import com.qubit.solution.fenixedu.integration.ldap.service.LdapIntegration;

public class SendPersonToLdapTask extends CustomTask {

    @Override
    public void runTask() throws Exception {
        for (Person person : Bennu.getInstance().getPartysSet().stream().filter(p -> p instanceof Person).map(Person.class::cast)
                .collect(Collectors.toList())) {
            if (!LdapIntegration.isPersonAvailableInLdap(person)) {
                LdapIntegration.createPersonInLdap(person);
                Student student = person.getStudent();
                if (student != null) {
                    LdapIntegration.updateStudentStatus(student);
                }
            }
        }

    }

}
