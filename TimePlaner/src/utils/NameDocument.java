
// software for charitable associations 
// Copyright (C) 2010  Artem Petrov (artpetro@uos.de) & Leonid Oldenburger (loldenbu@uos.de)

// This program is free software; you can redistribute it and/or modify it under the terms 
// of the GNU General Public License as published by the Free Software Foundation; either 
// version 3 of the License, or (at your option) any later version.

// This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
// without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.

// You should have received a copy of the GNU General Public License along with this program; 
// if not, see <http://www.gnu.org/licenses/>.
package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class NameDocument extends PlainDocument {
	
	private int limit;
	
	public NameDocument(int limit) {
		this.limit = limit;
	}

	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		
		for(int i = 0; i < str.length(); i++) {
			
			if((getLength() + str.length()) > limit) {
				return;
			}
				
			char c = str.charAt(i);
			if (c == '|')
				return;
		
		}

		super.insertString(offs, str, a);
	}

}
