package de.mle.stackoverflow.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class MemberTest {
	@InjectMocks
	private Member member;
	@Mock
	private MemberDao memberDao;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void createId() throws Exception {
		MembersIdDto dto = new MembersIdDto();
		dto.setId("967405286");
		when(memberDao.findNext()).thenReturn(dto);

		assertThat(member.createNewId()).isEqualTo("967405286");
	}
}