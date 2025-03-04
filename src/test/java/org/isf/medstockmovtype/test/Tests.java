/*
 * Open Hospital (www.open-hospital.org)
 * Copyright © 2006-2023 Informatici Senza Frontiere (info@informaticisenzafrontiere.org)
 *
 * Open Hospital is a free and open source software for healthcare data management.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package org.isf.medstockmovtype.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.isf.OHCoreTestCase;
import org.isf.medstockmovtype.manager.MedicalDsrStockMovementTypeBrowserManager;
import org.isf.medstockmovtype.model.MovementType;
import org.isf.medstockmovtype.service.MedicalDsrStockMovementTypeIoOperation;
import org.isf.medstockmovtype.service.MedicalDsrStockMovementTypeIoOperationRepository;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class Tests extends OHCoreTestCase {

	private static TestMovementType testMovementType;

	@Autowired
	MedicalDsrStockMovementTypeIoOperation medicalDsrStockMovementTypeIoOperation;
	@Autowired
	MedicalDsrStockMovementTypeIoOperationRepository medicalDsrStockMovementTypeIoOperationRepository;
	@Autowired
	MedicalDsrStockMovementTypeBrowserManager medicalDsrStockMovementTypeBrowserManager;

	@BeforeClass
	public static void setUpClass() {
		testMovementType = new TestMovementType();
	}

	@Before
	public void setUp() {
		cleanH2InMemoryDb();
	}

	@Test
	public void testMovementTypeGets() throws Exception {
		String code = setupTestMovementType(false);
		checkMovementTypeIntoDb(code);
	}

	@Test
	public void testMovementTypeSets() throws Exception {
		String code = setupTestMovementType(true);
		checkMovementTypeIntoDb(code);
	}

	@Test
	public void testIoFindOne() throws Exception {
		String code = setupTestMovementType(false);
		MovementType foundMovementType = medicalDsrStockMovementTypeIoOperation.findOneByCode(code);
		List<MovementType> movementTypes = medicalDsrStockMovementTypeIoOperation.getMedicalDsrStockMovementType();
		assertThat(movementTypes.get(movementTypes.size() - 1).getDescription()).isEqualTo(foundMovementType.getDescription());
	}

	@Test
	public void testIoFindOneNull() throws Exception {
		assertThat(medicalDsrStockMovementTypeIoOperation.findOneByCode("notThere")).isNull();
	}

	@Test
	public void testIoUpdateMovementType() throws Exception {
		String code = setupTestMovementType(false);
		MovementType foundMovementType = medicalDsrStockMovementTypeIoOperation.findOneByCode(code);
		foundMovementType.setDescription("Update");
		MovementType result = medicalDsrStockMovementTypeIoOperation.updateMedicalDsrStockMovementType(foundMovementType);
		assertThat(result).isNotNull();
		MovementType updateMovementType = medicalDsrStockMovementTypeIoOperation.findOneByCode(code);
		assertThat(updateMovementType.getDescription()).isEqualTo("Update");
	}

	@Test
	public void testIoNewMovementType() throws Exception {
		MovementType movementType = testMovementType.setup(true);
		MovementType result = medicalDsrStockMovementTypeIoOperation.newMedicalDsrStockMovementType(movementType);
		assertThat(result).isNotNull();
		checkMovementTypeIntoDb(movementType.getCode());
	}

	@Test
	public void testIoIsCodePresent() throws Exception {
		String code = setupTestMovementType(false);
		boolean result = medicalDsrStockMovementTypeIoOperation.isCodePresent(code);
		assertThat(result).isTrue();
	}

	@Test
	public void testIoDeleteMovementType() throws Exception {
		String code = setupTestMovementType(false);
		MovementType foundMovementType = medicalDsrStockMovementTypeIoOperation.findOneByCode(code);
		boolean result = medicalDsrStockMovementTypeIoOperation.deleteMedicalDsrStockMovementType(foundMovementType);
		assertThat(result).isTrue();
		result = medicalDsrStockMovementTypeIoOperation.isCodePresent(code);
		assertThat(result).isFalse();
	}

	@Test
	public void testMgrFindOne() throws Exception {
		String code = setupTestMovementType(false);
		MovementType foundMovementType = medicalDsrStockMovementTypeBrowserManager.getMovementType(code);
		List<MovementType> movementTypes = medicalDsrStockMovementTypeBrowserManager.getMedicalDsrStockMovementType();
		assertThat(movementTypes.get(movementTypes.size() - 1).getDescription()).isEqualTo(foundMovementType.getDescription());
	}

	@Test
	public void testMgrFindOneNull() throws Exception {
		assertThat(medicalDsrStockMovementTypeBrowserManager.getMovementType("notThere")).isNull();
	}

	@Test
	public void testMgrUpdateMovementType() throws Exception {
		String code = setupTestMovementType(false);
		MovementType foundMovementType = medicalDsrStockMovementTypeBrowserManager.getMovementType(code);
		foundMovementType.setDescription("Update");
		assertThat(medicalDsrStockMovementTypeBrowserManager.updateMedicalDsrStockMovementType(foundMovementType)).isNotNull();
		MovementType updateMovementType = medicalDsrStockMovementTypeBrowserManager.getMovementType(code);
		assertThat(updateMovementType).isNotNull();
		assertThat(updateMovementType.getDescription()).isEqualTo("Update");
	}

	@Test
	public void testMgrNewMovementType() throws Exception {
		MovementType movementType = testMovementType.setup(true);
		assertThat(medicalDsrStockMovementTypeBrowserManager.newMedicalDsrStockMovementType(movementType)).isNotNull();
		checkMovementTypeIntoDb(movementType.getCode());
	}

	@Test
	public void testMgrIsCodePresent() throws Exception {
		String code = setupTestMovementType(false);
		assertThat(medicalDsrStockMovementTypeBrowserManager.isCodePresent(code)).isTrue();
	}

	@Test
	public void testMgrDeleteMovementType() throws Exception {
		String code = setupTestMovementType(false);
		MovementType foundMovementType = medicalDsrStockMovementTypeBrowserManager.getMovementType(code);
		assertThat(medicalDsrStockMovementTypeBrowserManager.deleteMedicalDsrStockMovementType(foundMovementType)).isTrue();
		assertThat(medicalDsrStockMovementTypeBrowserManager.isCodePresent(code)).isFalse();
	}

	@Test
	public void testMgrMovementTypeValidationNoKey() throws Exception {
		assertThatThrownBy(() ->
		{
			MovementType movementType = new MovementType("", "TestDescription", "+");
			medicalDsrStockMovementTypeBrowserManager.newMedicalDsrStockMovementType(movementType);
		})
				.isInstanceOf(OHDataValidationException.class);
	}

	@Test
	public void testMgrMovementTypeValidationKeyTooLong() throws Exception {
		assertThatThrownBy(() ->
		{
			MovementType movementType = new MovementType("abcdefghijklmnopqrstuvwxyz", "TestDescription", "+");
			medicalDsrStockMovementTypeBrowserManager.newMedicalDsrStockMovementType(movementType);
		})
				.isInstanceOf(OHDataValidationException.class);
	}

	@Test
	public void testMgrMovementTypeValidationTypeTooLong() throws Exception {
		assertThatThrownBy(() ->
		{
			MovementType movementType = new MovementType("ZZABCD", "TestDescription", "+++++");
			medicalDsrStockMovementTypeBrowserManager.newMedicalDsrStockMovementType(movementType);
		})
				.isInstanceOf(OHDataValidationException.class);
	}

	@Test
	public void testMgrMovementTypeValidationNoDescription() throws Exception {
		assertThatThrownBy(() ->
		{
			MovementType movementType = new MovementType("ZZABCD", "", "+");
			medicalDsrStockMovementTypeBrowserManager.newMedicalDsrStockMovementType(movementType);
		})
				.isInstanceOf(OHDataValidationException.class);
	}

	@Test
	public void testMgrMovementTypeValidationCodeAlreadyExists() throws Exception {
		assertThatThrownBy(() ->
		{
			MovementType movementType = testMovementType.setup(false);
			medicalDsrStockMovementTypeIoOperationRepository.saveAndFlush(movementType);
			MovementType movementType2 = new MovementType(movementType.getCode(), movementType.getDescription(), movementType.getType());
			medicalDsrStockMovementTypeBrowserManager.newMedicalDsrStockMovementType(movementType2);
		})
				.isInstanceOf(OHDataIntegrityViolationException.class);
	}

	@Test
	public void testMovementTypeToString() throws Exception {
		MovementType movementType = new MovementType("ZZABCD", "TestDescription", "+");
		assertThat(movementType).hasToString("TestDescription");
	}

	@Test
	public void testMovementTypeEquals() throws Exception {
		MovementType movementType1 = new MovementType("ZZABCD", "TestDescription", "+");
		MovementType movementType2 = new MovementType("ABCDZZ", "TestDescription", "+");
		MovementType movementType3 = new MovementType("ZZABCD", "AnotherDescription", "+");
		MovementType movementType4 = new MovementType("ZZABCD", "TestDescription", "++");

		assertThat(movementType1).isEqualTo(movementType1);
		assertThat(movementType1)
				.isNotEqualTo("someString")
				.isNotEqualTo(movementType2)
				.isEqualTo(movementType3)
				.isEqualTo(movementType4);
	}

	@Test
	public void testMovementTypeHashCode() {
		MovementType movementType = new MovementType("ZZABCD", "TestDescription", "+");
		// generate hashCode
		int hashCode = movementType.hashCode();
		assertThat(hashCode).isNotZero();
		// used computed value
		assertThat(movementType.hashCode()).isEqualTo(hashCode);
	}

	private String setupTestMovementType(boolean usingSet) throws OHException {
		MovementType movementType = testMovementType.setup(usingSet);
		medicalDsrStockMovementTypeIoOperationRepository.saveAndFlush(movementType);
		return movementType.getCode();
	}

	private void checkMovementTypeIntoDb(String code) throws OHException {
		MovementType foundMovementType = medicalDsrStockMovementTypeIoOperation.findOneByCode(code);
		testMovementType.check(foundMovementType);
	}
}