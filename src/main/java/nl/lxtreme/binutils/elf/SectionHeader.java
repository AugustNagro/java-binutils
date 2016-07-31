/*
 * BinUtils - access various binary formats from Java
 *
 * (C) Copyright 2016 - JaWi - j.w.janssen@lxtreme.nl
 *
 * Licensed under Apache License v2. 
 */
package nl.lxtreme.binutils.elf;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Represents information about the various sections in an ELF object.
 */
public class SectionHeader {
	private int nameOffset;
	private String name;
	private long offsetShift = 0;
	private long size;

	public SectionType type;
	public final long flags;
	public final long virtualAddress;
	public final long offsetOfVirtualAddress;
	public final long fileOffset;
	public final long offsetOfFileOffset;
	public final long offsetOfSize;
	public final int link;
	public final int info;
	public final long sectionAlignment;
	public final long entrySize;

	public SectionHeader(ElfClass elfClass, ByteBuffer buf, long sectionHeaderOffset) throws IOException {
		nameOffset = buf.getInt();
		type = SectionType.valueOf(buf.getInt());

		if (elfClass == ElfClass.CLASS_32) {
			flags = buf.getInt() & 0xFFFFFFFFL;
			virtualAddress = buf.getInt() & 0xFFFFFFFFL;
			offsetOfVirtualAddress = sectionHeaderOffset + buf.position() - Integer.BYTES;
			fileOffset = buf.getInt() & 0xFFFFFFFFL;
			offsetOfFileOffset = sectionHeaderOffset + buf.position() - Integer.BYTES;
			size = buf.getInt() & 0xFFFFFFFFL;
			offsetOfSize = sectionHeaderOffset + buf.position() - Integer.BYTES;
		} else if (elfClass == ElfClass.CLASS_64) {
			flags = buf.getLong();
			virtualAddress = buf.getLong();
			offsetOfVirtualAddress = sectionHeaderOffset + buf.position() - Long.BYTES;
			fileOffset = buf.getLong();
			offsetOfFileOffset = sectionHeaderOffset + buf.position() - Long.BYTES;
			size = buf.getLong();
			offsetOfSize = sectionHeaderOffset + buf.position() - Long.BYTES;
		} else {
			throw new IOException("Unhandled ELF-class!");
		}

		link = buf.getInt();
		info = buf.getInt();

		if (elfClass == ElfClass.CLASS_32) {
			sectionAlignment = buf.getInt() & 0xFFFFFFFFL;
			entrySize = buf.getInt() & 0xFFFFFFFFL;
		} else if (elfClass == ElfClass.CLASS_64) {
			sectionAlignment = buf.getLong();
			entrySize = buf.getLong();
		} else {
			throw new IOException("Unhandled ELF-class!");
		}
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getSize() {
		return size;
	}

	public void setOffsetShift(long offsetShift) {
		this.offsetShift = offsetShift;
	}

	public long getOffsetShift() {
		return offsetShift;
	}

	public String getName() {
		return name;
	}

	void setName(ByteBuffer buf) {
		if (nameOffset > 0) {
			byte[] array = buf.array();

			int end = nameOffset;
			while (end < array.length && array[end] != 0) {
				end++;
			}

			name = new String(array, nameOffset, end - nameOffset);
		}
	}
}
