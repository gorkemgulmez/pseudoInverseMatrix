package matrix;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

	private final double[][] nonSquareMatrix =
	{
		{1,2},
		{3,4},
		{5,6}
	};

	private final double[][] squareMatrix =
	{
		{1,2,3},
		{0,1,4},
		{5,6,0}
	};

	@Test
	void transpose() {
		double[][] expected =
		{
			{1,3,5},
			{2,4,6}
		};
		double[][] output = Matrix.transpose(nonSquareMatrix);
		assertArrayEquals(expected, output);
	}

	@Test
	void multiplication() {
		double[][] output = Matrix.multiplication(nonSquareMatrix, Matrix.transpose(nonSquareMatrix));
		double[][] expected =
		{
			{5,11,17},
			{11,25,39},
			{17,39,61},
		};
		assertArrayEquals(expected, output);
	}

	@Test
	void squareInverse() {
		double[][] output = Matrix.squareInverse(squareMatrix);
		double[][] expected =
		{
			{-24,   18,     5},
			{20,    -15,    -4},
			{-5,    4,      1}
		};
		assertArrayEquals(expected, output);
	}

	@Test
	void det() {
		double output = Matrix.det(1, squareMatrix);
		double expected = 1;
		assertEquals(expected, output);
	}

	@Test
	void subMatrix() {
		double[][] output = Matrix.subMatrix(0, squareMatrix);
		double[][] expected =
		{
			{1,4},
			{6,0}
		};
		assertArrayEquals(expected, output);
	}

	@Test
	void cofactor() {
		double[][] output = Matrix.cofactor(squareMatrix);
		double[][] expected =
				{
						{-24,   20,     -5},
						{18,    -15,    4},
						{5,    -4,      1}
				};
		assertArrayEquals(expected, output);
	}

	@Test
	void adjugate() {
		double[][] output = Matrix.cofactor(squareMatrix);
		double[][] expected =
				{
						{-24,   20,     -5},
						{18,    -15,    4},
						{5,    -4,      1}
				};
		assertArrayEquals(expected, output);
	}

	@Test
	void pseudoInverse() {
		double[][] output = Matrix.pseudoInverse(nonSquareMatrix);
		double[][] expected =
				{
						{-1.3333333333333335, -0.3333333333333339, 0.6666666666666643},
						{1.0833333333333333, 0.33333333333333304, -0.4166666666666661}
				};
		assertArrayEquals(expected, output);
	}
}