package com.osl.automation.facility;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class ElementProvider{

	public class WebList extends Select {
		private WebList(WebElement element) {
			super(element);
			// TODO Auto-generated constructor stub
		}

		public int getItemsCount() {
			int count = getOptions().size();
			return count;
		}

		public void selectRandomItem() {
			int itemCount = getItemsCount();
			int randomItem = Utility.generateRandomNumber(1, itemCount - 1);
			getOptions().get(randomItem).click();
		}
	}

	public class WebRadioBtn {
		private List<WebElement> radioBtns;

		private WebRadioBtn(List<WebElement> elements) {
			this.radioBtns = elements;
			for (WebElement element : elements) {
				String type = element.getAttribute("type");
				if (null == type || !"radio".equals(type.toLowerCase())) {
					throw new NoSuchElementException(
							"Cannot find the radio buttons.");
				}
			}
		}

		private WebElement getRadioBtn(String value) {
			boolean forFlag = false;
			WebElement radioBtn = null;
			String actualValue = null;

			x: for (WebElement element : radioBtns) {
				actualValue = element.getAttribute("value");
				if (actualValue.equalsIgnoreCase(value)) {
					radioBtn = element;
					forFlag = true;
					break x;
				}
			}
			
			if (!forFlag) {
				throw new NoSuchElementException("No such value: "
						+ actualValue);
			}
			return radioBtn;
		}

		public void selectByValue(String value) {
			getRadioBtn(value).click();
		}
	}

	public class WebCheckBox {
		private List<WebElement> checkboxes;

		private WebCheckBox(List<WebElement> elements) {
			this.checkboxes = elements;
			for (WebElement element : elements) {
				String type = element.getAttribute("type");
				if (null == type || !"checkbox".equals(type.toLowerCase())) {
					throw new NoSuchElementException(
							"Cannot find the checkboxes.");
				}
			}
		}

		private WebElement getCheckBox(String value) {
			boolean forFlag = false;
			WebElement checkbox = null;
			String actualValue = null;

			x: for (WebElement element : checkboxes) {
				actualValue = element.getAttribute("value");
				if (actualValue.equals(value.toLowerCase())) {
					checkbox = element;
					forFlag = true;
					break x;
				}
			}
			if (!forFlag) {
				throw new NoSuchElementException("No such value: "
						+ actualValue);
			}
			return checkbox;
		}

		private WebElement getCheckBoxes(String[] values) {
			boolean forFlag = false;
			WebElement checkbox = null;
			String actualValue = null;

			x: for (WebElement element : checkboxes) {
				actualValue = element.getAttribute("value");
				for (String value : values) {
					if (actualValue.equals(value.toLowerCase())) {
						checkbox = element;
						forFlag = true;
						break x;
					}
				}
			}
			if (!forFlag) {
				throw new NoSuchElementException("No such value: "
						+ actualValue);
			}
			return checkbox;
		}

		public void selectByValue(String value) {
			getCheckBox(value).click();
		}

		public void selectMultipleByValues(String[] values) {
			getCheckBoxes(values).click();
		}

		public void selectRandomItem() {
			boolean endFlag = false;
			int execCount = 0;
			int itemCount = checkboxes.size();
			while (endFlag == false || execCount == 5) {
				int randomItem = Utility.generateRandomNumber(0, itemCount - 1);
				if (!checkboxes.get(randomItem).isSelected()
						&& checkboxes.get(randomItem).isEnabled()) {
					checkboxes.get(randomItem).click();
					endFlag = true;
				} else {
					execCount++;
				}
			}
		}

		public void selectAllItems() {
			for (WebElement checkbox : checkboxes) {
				checkbox.click();
			}
		}

		public void selectRandomItems(int count) {
			boolean endFlag = false;
			int execCount = 0;
			for (int i = 0; i < count; i++) {
				int itemCount = checkboxes.size();
				while (endFlag == false || execCount == 5) {
					int randomItem = Utility.generateRandomNumber(0,
							itemCount - 1);
					if (!checkboxes.get(randomItem).isSelected()
							&& checkboxes.get(randomItem).isEnabled()) {
						checkboxes.get(randomItem).click();
						endFlag = true;
					} else {
						execCount++;
					}
				}
			}
		}
	}

	public class WebTable {
		List<WebElement> cols;
		List<WebElement> rows;
		List<WebElement> subTable;

		private List<WebElement> getSubTable() {
			return subTable;
		}

		private void setSubTable(List<WebElement> subTable) {
			this.subTable = subTable;
		}

		private List<WebElement> getCols() {
			return cols;
		}

		private void setCols(List<WebElement> cols) {
			this.cols = cols;
		}

		private List<WebElement> getRows() {
			return rows;
		}

		private void setRows(List<WebElement> rows) {
			this.rows = rows;
		}

		private WebTable(WebElement element) {
			if (!element.getTagName().equalsIgnoreCase("table")) {
				throw new NoSuchElementException("Cannot find the webtable");
			} else {
				setSubTable(element.findElements(By.cssSelector("tbody")));
				setRows(element.findElements(By.cssSelector("*>tr")));
				setCols(element.findElements(By
						.cssSelector("*:first-child>tr>*")));
			}
		}

		public int getRowCount() {
			int rowCount = 0;
			int tempRow = 0;
			if (getSubTable().size() == 1) {
				rowCount = getRows().size();
			} else {
				for (int i = 1; i < getSubTable().size(); i++) {
					tempRow = tempRow
							+ getSubTable().get(i)
									.findElements(By.cssSelector("tbody>*"))
									.size();
				}
				rowCount = getRows().size() - tempRow;
			}
			return rowCount;
		}

		public int getColCount() {
			int colCount = 0;
			int tempCol = 0;
			if (getSubTable().size() == 1) {
				return getCols().size();
			} else {
				for (int i = 1; i < getSubTable().size(); i++) {
					tempCol = tempCol
							+ getSubTable()
									.get(i)
									.findElements(
											By.cssSelector("tbody>*:first-child>:nth-child(n)"))
									.size();
				}
				colCount = getCols().size() - tempCol;
			}
			return colCount;
		}

		public WebElement getCellElementByRowCol(int row, int col) {
			WebElement element;

			if (row == 1) {
				element = getCols().get(col - 1);
			} else {
				element = getRows().get(row - 1)
						.findElements(By.cssSelector("td")).get(col - 1);
			}
			return element;
		}
	}

	public WebCheckBox webCheckBoxes(List<WebElement> elements) {
		return new WebCheckBox(elements);
	}

	public WebRadioBtn webRadioBtns(List<WebElement> elements) {
		return new WebRadioBtn(elements);
	}

	public WebList webList(WebElement element) {
		return new WebList(element);
	}

	public WebTable webTable(WebElement element) {
		return new WebTable(element);
	}
}
